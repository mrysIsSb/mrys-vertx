package top.mrys.vertx.http.parser;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.SneakyThrows;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author mrys
 * @date 2020/7/4
 */
public class RouteFactory {

  private final List<Parser<ControllerMethodWrap, Router>> parsers
      = Arrays.asList(new SimpleHandlerParser(),
      new GeneralMethodParser()
  );

  @SneakyThrows
  public Router getRouter(Vertx vertx, Class[] classes) {
    Router router = Router.router(vertx);
    for (Class clazz : classes) {
      if (AnnotationUtil.isHaveAnyAnnotations(clazz, RouteHandler.class)) {
        RouteMapping mapping = (RouteMapping) clazz.getAnnotation(RouteMapping.class);
        Router sonRouter;
        if (Objects.nonNull(mapping)) {
          sonRouter = Router.router(vertx);
        } else {
          sonRouter = router;
        }
        Method[] methods = AnnotationUtil
            .getMethodByAnnotation(clazz, RouteMapping.class);
        Object o = clazz.newInstance();
        for (Method method : methods) {
          ControllerMethodWrap wrap = new ControllerMethodWrap(method, clazz,
              o);
          Parser<ControllerMethodWrap, Router> p = null;
          for (Parser<ControllerMethodWrap, Router> parser : parsers) {
            if (parser.canExec(wrap)) {
              p = parser;
              break;
            }
          }
          if (Objects.isNull(p)) {
            continue;
          }
          p.accept(wrap, sonRouter);
        }
        if (Objects.nonNull(mapping)) {
          router.mountSubRouter(mapping.value(), sonRouter);
        }
      }
    }
    return router;
  }
}
