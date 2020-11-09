package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author mrys
 * @date 2020/9/17
 */
public interface MyConfigStoreTk {

  String defName = "configuration";


  /**
   * 获取store的type 用在配置文件的 storeType
   *
   * @author mrys
   */
  String getStoreName();

  /**
   * 检查是否可用;
   *
   * @author mrys
   */
  boolean check(JsonObject configuration);

  ConfigStore create(Vertx vertx, JsonObject configuration);

  /**
   * key 和 profile 拼接方法
   *
   * @author mrys
   */
  BiFunction<String, String, String> getJointKeyAndProfile();

  /**
   * 获取处理后的配置文件
   *
   * @author mrys
   */
  default JsonObject getDealPostConfig(JsonObject configuration) {
    JsonObject config = configuration.getJsonObject("config");
    JsonArray profiles = MyConfigStoreTk.getProfiles(configuration);
    JsonArray keys = config.getJsonArray("keys", new JsonArray("[\"" + defName + "\"]"));
    if (profiles != null && !profiles.isEmpty()) {
      List<String> collect = keys.stream().map(Object::toString).flatMap(ks ->
          profiles.stream().map(Object::toString)
              .map(s -> getJointKeyAndProfile().apply(ks, s))
      ).collect(Collectors.toList());
      keys.addAll(new JsonArray(collect));
    }
    return config;
  }

//---------------------------static method

  /**
   * 获取环境
   *
   * @author mrys
   */
  static JsonArray getProfiles(JsonObject config) {
    return config.getJsonArray("profiles");
  }

  /**
   * 这个是相当于配置的key 的默认值
   *
   * @author mrys
   */
  static String getDefName() {
    return defName;
  }


}
