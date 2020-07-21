package top.mrys.vertx.http.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Method;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * @author mrys
 * @date 2020/7/9
 */
public class GeneralMethodParser implements Parser<ControllerMethodWrap, Router>{
    /**
     * 是否执行
     *
     * @param wrap
     * @author mrys
     */
    @Override
    public Boolean canExec(ControllerMethodWrap wrap) {
        Method method = wrap.getMethod();
        return !Handler.class.isAssignableFrom(method.getReturnType());
    }

    @Override
    public void accept(ControllerMethodWrap wrap, Router router) {
        Method method = wrap.getMethod();
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        MethodAccess methodAccess = MethodAccess.get(wrap.getClazz());
        int index = methodAccess.getIndex(name, parameterTypes);
        RouteMapping annotation = method.getAnnotation(RouteMapping.class);
        String value = annotation.value();
        EnumHttpMethod enumHttpMethod = annotation.method();
        Handler<RoutingContext> handler = event -> {
            ObjectMapper mapper = new ObjectMapper();
            Object o = methodAccess.invoke(wrap.getObject(), index);
            try {
                event.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=utf-8").end(mapper.writeValueAsString(o));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        };
        router.route(enumHttpMethod.getHttpMethod(),value).blockingHandler(handler);
    }
}
