package top.mrys.vertx.http.parser;

import cn.hutool.http.HttpStatus;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.BodyHandlerImpl;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.factorys.JsonTransverterFactory;
import top.mrys.vertx.common.manager.EnumJsonTransverterNameProvider;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * 返回为future
 *
 * @author mrys
 * @date 2020/7/9
 */
@Slf4j
public class FutureMethodParser extends AbstractHandlerParser {

  /**
   * 是否执行 返回为future
   *
   * @param wrap
   * @author mrys
   */
  @Override
  public Boolean canExec(ControllerMethodWrap wrap) {
    Method method = wrap.getMethod();
    return Future.class.isAssignableFrom(method.getReturnType());
  }

  @Override
  public void accept(ControllerMethodWrap wrap, Router router) {
    Method method = wrap.getMethod();
    String[] paths = getPath(wrap);
    RouteMapping annotation = getRouteMapping(method);
    EnumHttpMethod enumHttpMethod = annotation.method();
    HttpParameterFactory factory = HttpParameterFactory.getInstance(wrap);
    Handler<RoutingContext> handler = event -> {
      try {
        Object o = method.invoke(wrap.getObject(), factory.getHttpParameter(event));
        Future<Object> future = o instanceof Future ? ((Future) o) : null;
        HttpServerResponse response = event.response();
        response.setStatusCode(HttpStatus.HTTP_OK);
        response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        if (future != null) {
          future.onSuccess(re -> response.end(
              JsonTransverterFactory
                  .getJsonTransverter(EnumJsonTransverterNameProvider.http_server)
                  .serialize(re)))
              .onFailure(event::fail);
        } else {
          event.fail(500);
        }
      } catch (Exception e) {
        event.fail(e);
      }
    };
    for (String path : paths) {
      router.route(enumHttpMethod.getHttpMethod(), path).handler(handler);
    }
  }

}
