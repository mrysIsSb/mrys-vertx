package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author mrys
 * @date 2020/9/17
 */
public interface MyConfigStoreTk {

  /**
   * 获取store的type
   *
   * @author mrys
   */
  String getStoreName();

  /**
   * 严重是否可用;
   *
   * @author mrys
   */
  boolean check(JsonObject configuration);

  ConfigStore create(Vertx vertx, JsonObject configuration);
}
