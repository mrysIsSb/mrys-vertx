package top.mrys.vertx.http.parser;

import io.vertx.core.Handler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import top.mrys.vertx.common.utils.TypeUtil;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author mrys
 * @date 2020/7/9
 */
public class SimpleHandlerParser extends AbstractHandlerParser {

  /**
   * 是否执行 (返回类型为 handler)
   *
   * @author mrys
   * @see Handler
   */
  @Override
  public Boolean canExec(ControllerMethodWrap wrap) {
    Method method = wrap.getMethod();
    Class<?> returnType = method.getReturnType();
    if (Handler.class.isAssignableFrom(returnType)) {
      Type type = method.getGenericReturnType();
      ParameterizedType parameterizedType = TypeUtil.getParameterizedType(type);
      if (Objects.nonNull(parameterizedType)) {
        Type[] arguments = parameterizedType.getActualTypeArguments();
        if (arguments.length == 1) {
          return RoutingContext.class.isAssignableFrom(TypeUtil.getClass(arguments[0]));
        }
      }
    }
    return false;
  }

  @SneakyThrows
  @Override
  public void accept(ControllerMethodWrap wrap, Router router) {
    Method method = wrap.getMethod();
    RouteMapping annotation = method.getAnnotation(RouteMapping.class);
    String value = annotation.value();
    EnumHttpMethod enumHttpMethod = annotation.method();
    Handler<RoutingContext> handler = (Handler<RoutingContext>) method.invoke(wrap.getObject());
    router.route(enumHttpMethod.getHttpMethod(), value).handler(handler);
  }

}
