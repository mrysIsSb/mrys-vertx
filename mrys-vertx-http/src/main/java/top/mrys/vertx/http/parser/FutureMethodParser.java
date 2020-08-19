package top.mrys.vertx.http.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * 返回为future
 *
 * @author mrys
 * @date 2020/7/9
 */
@Component
public class FutureMethodParser extends AbstractHandlerParser{

    @Autowired
    private JsonTransverter jsonTransverter;
    /**
     * 是否执行
     * 返回为future
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
        RouteMapping annotation = getRouteMapping(method);
        String value = annotation.value();
        EnumHttpMethod enumHttpMethod = annotation.method();
        Handler<RoutingContext> handler = event -> {
            try {
                Object o = method.invoke(wrap.getObject());
                Future future = o instanceof Future ? ((Future) o) : null;
                HttpServerResponse response = event.response();
                response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8");
                future.onComplete(re -> response.end(jsonTransverter.serialize(((AsyncResult)re).result())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        router.route(enumHttpMethod.getHttpMethod(),value).blockingHandler(handler);
    }

}
