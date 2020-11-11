package top.mrys.vertx.http.parser;

import cn.hutool.core.util.StrUtil;
import io.vertx.ext.web.Router;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.springframework.core.annotation.AnnotatedElementUtils;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/8/19
 */
public abstract class AbstractHandlerParser implements Parser<ControllerMethodWrap, Router> {

  protected static RouteMapping getRouteMapping(Method method) {
    return AnnotatedElementUtils.findMergedAnnotation(method, RouteMapping.class);
  }

  protected static String[] getPath(ControllerMethodWrap wrap) {
    Method method = wrap.getMethod();
    RouteMapping mapping = getRouteMapping(method);
    String[] value = mapping.value();
    if (value.length == 0) {
      return new String[]{"/" + method.getName()};
    }
    return Arrays.stream(value).filter(StrUtil::isNotBlank)
        .map(path -> path.startsWith("/") ? path : "/" + path)
        .distinct().toArray(String[]::new);
  }
}
