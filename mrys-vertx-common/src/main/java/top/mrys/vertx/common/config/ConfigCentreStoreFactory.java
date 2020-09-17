package top.mrys.vertx.common.config;

import cn.hutool.core.util.StrUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.ServiceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Slf4j
public class ConfigCentreStoreFactory implements ConfigStoreFactory {


  public static final String configCentre = "configCentre";



  private final HashMap<String, MyConfigStoreTk> map = new HashMap<>();

  private final ConfigStore emptyConfigStore=new EmptyConfigStore();

  public ConfigCentreStoreFactory() {
    ServiceLoader<MyConfigStoreTk> load =ServiceLoader.load(MyConfigStoreTk.class);
    load.forEach(myConfigStoreTk -> map.put(myConfigStoreTk.getStoreName(),myConfigStoreTk));
  }

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
    String type = configuration.getString("storeType");
    if (StrUtil.isBlank(type)) {
      return emptyConfigStore;
    }
    MyConfigStoreTk storeTk = map.get(type);
    if (storeTk.check(configuration)) {
      return storeTk.create(vertx, configuration);
    }
    return emptyConfigStore;
  }
}
