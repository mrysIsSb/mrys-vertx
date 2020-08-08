package top.mrys.vertx.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;

/**
 * @author mrys
 * @date 2020/8/8
 */
public class MyJsonUtil {

  public static <T> T getByPath(String jsonStr, String expression,Class<T> tClass) {
    if (!JSONUtil.isJson(jsonStr)) {
      throw new RuntimeException("不是json");
    }
    if (JSONUtil.isJsonObj(jsonStr)) {
      return Convert.convert(tClass, JSONUtil.getByPath(JSONUtil.parseObj(jsonStr), expression));
    } else if (JSONUtil.isJsonArray(jsonStr)) {
      return Convert.convert(tClass, JSONUtil.getByPath(JSONUtil.parseArray(jsonStr), expression));
    }
    return null;
  }
}
