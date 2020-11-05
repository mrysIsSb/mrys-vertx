package top.mrys.vertx.common.factorys;

import java.util.HashMap;
import java.util.Map;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.manager.JsonTransverterImpl;

/**
 * json转换器工厂方法
 *
 * @author mrys
 * @date 2020/11/5
 */
public class JsonTransverterFactory {

  private static final JsonTransverterFactory JSON_TRANSVERTER_FACTORY = new JsonTransverterFactory();


  private static final Map<String, JsonTransverter> jsonTransverterMap = new HashMap<>();

  private static final JsonTransverter defaultJsonTransverter = new JsonTransverterImpl();

  private JsonTransverterFactory() {
  }

  /**
   * 获取json转换器
   *
   * @author mrys
   */
  public static JsonTransverter getJsonTransverter(String name) {
    return jsonTransverterMap.getOrDefault(name, getDefault());
  }

  public static JsonTransverter getDefault() {
    return defaultJsonTransverter;
  }

  public static void putJsonTransverter(String name, JsonTransverter jsonTransverter) {
    jsonTransverterMap.put(name, jsonTransverter);
  }


}
