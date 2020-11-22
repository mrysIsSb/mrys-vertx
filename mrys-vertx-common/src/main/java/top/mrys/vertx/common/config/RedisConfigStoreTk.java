package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.function.BiFunction;
import lombok.extern.slf4j.Slf4j;

/**
 * redis 配置仓库
 *
 * @author mrys
 * @date 2020/9/17
 */
@Slf4j
public class RedisConfigStoreTk implements MyConfigStoreTk {


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

  /**
   * keys = keys+:profile
   *
   * @author mrys
   */
  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    return new MyRedisConfigStore(vertx, getDealPostConfig(configuration));
  }

  /**
   * key 和 profile 拼接方法
   *
   * @author mrys
   */
  @Override
  public BiFunction<String, String, String> getJointKeyAndProfile() {
    return (s, s2) -> s + ":" + s2;
  }

}
