package top.mrys.vertx.http.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * 通用方法的方式
 *
 * @author mrys
 * @date 2020/7/9
 */
@Slf4j
@Deprecated
public class GeneralMethodParser extends AbstractHandlerParser {

  /**
   * 是否执行
   *
   * @param wrap
   * @author mrys
   */
  @Override
  public Boolean canExec(ControllerMethodWrap wrap) {
    Method method = wrap.getMethod();
    return !Handler.class.isAssignableFrom(method.getReturnType()) && !Future.class
        .isAssignableFrom(method.getReturnType());
  }


  /*
          String name = method.getName();
          Class<?>[] parameterTypes = method.getParameterTypes();
      MethodAccess methodAccess = MethodAccess.get(wrap.getClazz());
      int index = methodAccess.getIndex(name, parameterTypes);
      RouteMapping annotation = method.getAnnotation(RouteMapping.class);
      String value = annotation.value();
      EnumHttpMethod enumHttpMethod = annotation.method();
      Handler<RoutingContext> handler = event -> {
          ObjectMapper mapper = new ObjectMapper();
          Object o = methodAccess.invoke(wrap.getObject(), index);*/
  @Override
  public void accept(ControllerMethodWrap wrap, Router router) {
    Method method = wrap.getMethod();
    RouteMapping annotation = AnnotatedElementUtils
        .findMergedAnnotation(method, RouteMapping.class);
    String value = annotation.value()[0];//todo
    EnumHttpMethod enumHttpMethod = annotation.method();
    Handler<RoutingContext> handler = event -> {
      ObjectMapper mapper = new ObjectMapper();
      try {
        Object o = method.invoke(wrap.getObject());
        event.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8")
            .end(mapper.writeValueAsString(o));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        event.fail(e);
      }
    };
    router.route(enumHttpMethod.getHttpMethod(), value).blockingHandler(handler);
  }
}
