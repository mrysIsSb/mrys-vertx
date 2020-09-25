package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author mrys
 * @date 2020/9/17
 */
@Slf4j
public class RedisConfigStoreTk implements MyConfigStoreTk {

  String defName = "configuration";

  /**
   * 获取store的type
   *
   * @author mrys
   */
  @Override
  public String getStoreName() {
    return "redis";
  }

  @Override
  public boolean check(JsonObject configuration) {
    try {
      getClass().getClassLoader().loadClass("io.vertx.redis.client.Redis");
    } catch (ClassNotFoundException e) {
      log.warn("io.vertx.redis.client.Redis 不存在");
      return false;
    }
    return true;
  }

  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    JsonObject config = configuration.getJsonObject("config");
    String active = configuration.getString("active");
    if (StringUtils.hasText(active)) {
      config.put("key", config.getString("key", defName) + ":" + active);
    }

    return new MyRedisConfigStore(vertx, config);
  }
}
