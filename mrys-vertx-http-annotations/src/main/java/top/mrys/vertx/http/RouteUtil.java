package top.mrys.vertx.http;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.http.HttpMethod;
import java.lang.reflect.Method;
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
  public static String getPath(Method m) {
    String url = "";
    RouteMapping annotation = AnnotationUtil
        .findMergedAnnotation(m.getDeclaringClass(), RouteMapping.class);
    if (annotation != null) {
      url = annotation.value();
      url = url.startsWith("/") ? url : "/" + url;
    }
    RouteMapping routeMapping = AnnotationUtil.findMergedAnnotation(m, RouteMapping.class);
    String url1 = "";
    if (routeMapping != null) {
      url1 = routeMapping.value();
      if (StrUtil.isBlank(url1)) {
        url1 = m.getName();
      }
      url1 = url1.startsWith("/") ? url1 : "/" + url1;
    }
    return url + url1;
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
