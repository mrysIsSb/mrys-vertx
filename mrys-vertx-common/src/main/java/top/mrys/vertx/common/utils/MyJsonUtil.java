package top.mrys.vertx.common.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author mrys
 * @date 2020/8/8
 */
public class MyJsonUtil {

  public static <T> T getByPath(String jsonStr, String expression, Class<T> tClass) {
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

  public static <T> T mapTo(String jsonStr, Type type) {
    if (!JSONUtil.isJson(jsonStr)) {
     return Convert.convert((Class<T>) type, jsonStr);
    }
    if (JSONUtil.isJsonObj(jsonStr)) {
      return JSONUtil.toBean(jsonStr, (Class<T>) type);
    } else if (JSONUtil.isJsonArray(jsonStr)) {
      Class clazz = Map.class;
      if (type instanceof ParameterizedType) {
        Type rawType = ((ParameterizedType) type).getRawType();
        if (List.class.isAssignableFrom((Class) rawType)) {
          Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
          if (actualTypeArguments.length == 1) {
            clazz = (Class) actualTypeArguments[0];
          }
        } else if (type instanceof Class) {
          clazz = (Class) type;
        }
      }
      return (T) JSONUtil.toList(JSONUtil.parseArray(jsonStr), clazz);
    }
    return null;
  }
}
