package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.springframework.util.StringUtils;

/**
 * @author mrys
 * @date 2020/9/18
 */
public class HttpConfigStoreTk implements MyConfigStoreTk {

  /**
   * 获取store的type
   *
   * @author mrys
   */
  @Override
  public String getStoreName() {
    return "http";
  }

  /**
   * 严重是否可用;
   *
   * @param configuration
   * @author mrys
   */
  @Override
  public boolean check(JsonObject configuration) {
    return true;
  }

  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    JsonObject config = configuration.getJsonObject("config");
    String active = configuration.getString("active");
    if (StringUtils.hasText(active)) {
      config.put("path", config.getString("path", "/") + ":" + active);
    }
    return new MyHttpConfigStore(vertx,config);
  }
}
