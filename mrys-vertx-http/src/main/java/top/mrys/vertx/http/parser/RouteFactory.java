package top.mrys.vertx.http.parser;

import cn.hutool.core.collection.CollectionUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.common.utils.Interceptor;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/7/4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class RouteFactory implements Supplier<Router>{

  private Vertx vertx;
  private List<Class> classes;
  private final List<Parser<ControllerMethodWrap, Router>> parsers
      = Arrays.asList(new SimpleHandlerParser(),
      new GeneralMethodParser()
  );
  private final ConcurrentLinkedDeque<Interceptor<RoutingContext,?>> interceptors = new ConcurrentLinkedDeque<>();

  public static RouteFactory create(Vertx vertx, List<Class> classes) {
    return create(vertx, classes, Collections.emptyList());
  }

  public static RouteFactory create(Vertx vertx, List<Class> classes,
      List<Parser<ControllerMethodWrap, Router>> parsers) {
    RouteFactory routeFactory = new RouteFactory();
    routeFactory.vertx = vertx;
    routeFactory.classes = classes;
    if (CollectionUtil.isNotEmpty(parsers)) {
      routeFactory.parsers.addAll(parsers);
    }
    routeFactory.interceptors.add(new HttpInterceptor());
    return routeFactory;
  }






  @SneakyThrows
  @Override
  public Router get() {
    Router router = Router.router(vertx);
    if (CollectionUtil.isNotEmpty(interceptors)) {
      router.route().handler(event -> {
        Iterator<Interceptor<RoutingContext, ?>> iterator = interceptors.iterator();
        for (; iterator.hasNext() ; ) {
          if (!iterator.next().preHandler(event)) {
            event.response().setStatusCode(400).putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON+";charset=utf-8").end("拒绝访问");
          }
        }
        event.next();
      });
    }
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
