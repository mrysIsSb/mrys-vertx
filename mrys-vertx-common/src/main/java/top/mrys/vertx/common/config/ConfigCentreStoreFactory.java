package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Slf4j
public class ConfigCentreStoreFactory implements ConfigStoreFactory {


  public static final String configCentre = "configCentre";

  /**
   * @return the name of the factory.
   */
  @Override
  public String name() {
    return configCentre;
  }

  /**
   * Creates an instance of the {@link ConfigStore}.
   *
   * @param vertx         the vert.x instance, never {@code null}
   * @param configuration the configuration, never {@code null}, but potentially empty
   * @return the created configuration store
   */
  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    String active = configuration.getString("active");
    String type = configuration.getString("storeType");
    JsonObject config = configuration.getJsonObject("config");
    if (StringUtils.hasText(active)) {
      config.put("key", config.getString("key", "configuration") + ":" + active);
    }
    if ("redis".equals(type)) {
      try {
        getClass().getClassLoader().loadClass("io.vertx.redis.client.Redis");
      } catch (ClassNotFoundException e) {
        log.error("redis 不存在");
        return new EmptyConfigStore();
      }
      return new MyRedisConfigStore(vertx, config);
    }
    return new EmptyConfigStore();
  }
}
