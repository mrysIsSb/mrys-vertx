package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.function.BiFunction;
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
    return new MyHttpConfigStore(vertx, getDealPostConfig(configuration));
  }

  /**
   * key 和 profile 拼接方法
   *
   * @author mrys
   */
  @Override
  public BiFunction<String, String, String> getJointKeyAndProfile() {
    return (s, s2) -> s + "/" + s2;
  }

}
