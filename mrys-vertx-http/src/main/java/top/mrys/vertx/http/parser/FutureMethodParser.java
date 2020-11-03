package top.mrys.vertx.http.parser;

import cn.hutool.http.HttpStatus;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.Method;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.manager.JsonTransverterImpl;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * 返回为future
 *
 * @author mrys
 * @date 2020/7/9
 */
public class FutureMethodParser extends AbstractHandlerParser {

  private JsonTransverter jsonTransverter;

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
    String path = getPath(wrap);
    RouteMapping annotation = getRouteMapping(method);
    EnumHttpMethod enumHttpMethod = annotation.method();
    HttpParameterFactory factory = HttpParameterFactory.getInstance(wrap);
    Handler<RoutingContext> handler = event -> {
      try {
        Promise<Object[]> p = Promise.promise();
        factory.getHttpParameter(event, p);
        Promise<Object> rep = Promise.promise();
        p.future().onSuccess(oarr -> {
          try {
            Object o = method.invoke(wrap.getObject(), oarr);
            Future future = o instanceof Future ? ((Future) o) : null;
            future.onComplete(rep);
          } catch (Exception e) {
            e.printStackTrace();
            event.fail(e);
          }
        });
        HttpServerResponse response = event.response();
        response.setStatusCode(HttpStatus.HTTP_OK);
        response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
        rep.future()
            .onComplete(re -> response.end(jsonTransverter.serialize(re.result())));
      } catch (Exception e) {
        e.printStackTrace();
        event.fail(e);
      }
    };
    router.route(enumHttpMethod.getHttpMethod(), path).handler(handler);
  }

}
