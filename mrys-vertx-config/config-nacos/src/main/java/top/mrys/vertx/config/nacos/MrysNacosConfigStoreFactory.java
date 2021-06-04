package top.mrys.vertx.config.nacos;

import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import top.mrys.vertx.config.common.StringConstant;

/**
 * @author mrys
 * 2021/6/2
 */
public class MrysNacosConfigStoreFactory implements ConfigStoreFactory {

  @Override
  public String name() {
    return StringConstant.nacos_type;
  }

  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    return new MrysNacosConfigStore(vertx, configuration);
  }
}
