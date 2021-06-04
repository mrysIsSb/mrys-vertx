package top.mrys.vertx.config.eventbus;

import java.util.Objects;

import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import top.mrys.vertx.config.common.StringConstant;

/**
 * @author mrys
 * 2021/5/26
 */
public class MrysEventBusConfigStoreFactory implements ConfigStoreFactory {


  @Override
  public String name() {
    return StringConstant.event_bus_type;
  }

  /**
   * 获取对应地址下的配置
   *
   * @author mrys
   */
  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    String address = configuration.getString("address");
    Objects.requireNonNull(address);
    return new MrysEventBusConfigStore(vertx, address);
  }
}
