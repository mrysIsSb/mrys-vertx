package top.mrys.vertx.http.parser;

import io.vertx.ext.web.Router;
import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotatedElementUtils;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/8/19
 */
public abstract class AbstractHandlerParser implements Parser<ControllerMethodWrap, Router> {

  protected static RouteMapping getRouteMapping(Method method) {
      return AnnotatedElementUtils.findMergedAnnotation(method,RouteMapping.class);
  }
}
