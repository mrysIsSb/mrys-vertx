package top.mrys.vertx.http;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.http.HttpMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * @author mrys
 * @date 2020/10/20
 */
public class RouteUtil {

  /**
   * 获取请求地址
   *
   * @author mrys
   */
  public static String[] getPath(Method m) {
    String url = "";
    RouteMapping annotation = AnnotationUtil
        .findMergedAnnotation(m.getDeclaringClass(), RouteMapping.class);
    if (annotation != null) {
      url = annotation.value()[0];
      url = url.startsWith("/") ? url : "/" + url;
    }
    RouteMapping routeMapping = AnnotationUtil.findMergedAnnotation(m, RouteMapping.class);
    String[] url1 = new String[0];
    if (routeMapping != null) {
      url1 = getPath(routeMapping, m.getName());
    }
    final String finalUrl = url;
    return Arrays.stream(url1).map(s -> finalUrl + s).distinct().toArray(String[]::new);
  }

  public static String[] getPath(RouteMapping mapping, String def) {
    String[] value = mapping.value();
    if (value.length == 0) {
      return new String[]{"/" + def};
    }
    return Arrays.stream(value).filter(StrUtil::isNotBlank)
        .map(path -> path.startsWith("/") ? path : "/" + path)
        .distinct().toArray(String[]::new);
  }

  /**
   * 获取请求方法
   *
   * @author mrys
   */
  public static HttpMethod getMethod(Method m) {
    RouteMapping routeMapping = AnnotationUtil.findMergedAnnotation(m, RouteMapping.class);
    if (routeMapping != null && !EnumHttpMethod.NONE.equals(routeMapping.method())) {
      return routeMapping.method().getHttpMethod();
    }
    if (ArrayUtil.isEmpty(m.getParameters())) {
      return HttpMethod.GET;
    }
    return HttpMethod.POST;
  }
}
