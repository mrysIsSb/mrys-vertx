package top.mrys.vertx.http.parser;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpStatus;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.BodyHandlerImpl;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.common.utils.Interceptor;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/7/4
 */
@Slf4j
public class DefaultRouteFactory implements RouteFactory<DefaultRouteFactory> {

  protected Vertx vertx;
  //class
  protected Set<Class> classes = new HashSet<>();
  //解析器
  protected final List<AbstractHandlerParser> parsers = new ArrayList<>();
  //拦截器
  protected final ConcurrentLinkedDeque<Interceptor<RoutingContext, ?>> interceptors = new ConcurrentLinkedDeque<>();
  //实例工厂
  protected ObjectInstanceFactory factory = ObjectInstanceFactory.getDefault();

  {
    parsers.add(new FutureMethodParser());
    parsers.add(new SimpleHandlerParser());
    parsers.add(new GeneralMethodParser());
  }

  private static Map<Vertx, Router> routerCache = new ConcurrentHashMap<>();

  /**
   * 添加要扫描的类
   *
   * @param classes
   * @author mrys
   */
  @Override
  public DefaultRouteFactory addClasses(Set<Class> classes) {
    this.classes.addAll(classes);
    return this;
  }

  /**
   * 添加对象工厂实例
   *
   * @param factory
   * @author mrys
   */
  @Override
  public DefaultRouteFactory addObjectInstanceFactory(ObjectInstanceFactory factory) {
    this.factory = factory;
    return this;
  }


  @SneakyThrows
  @Override
  public synchronized Router get() {
    Router router;
    if ((router = getFromCache()) != null) {
      return router;
    }
    router = Router.router(vertx);
    //全局异常请求
    router.route().failureHandler(this::getFailureHandler);

    router.route().handler(new BodyHandlerImpl());

    //前置拦截器 todo 后置
    if (CollectionUtil.isNotEmpty(interceptors)) {
      router.route().handler(this::getInterceptorHandler);
    }

    router.route().handler(event -> {
      log.debug("isFresh{}", event.isFresh());
      if (!event.response().isChunked()) {
        event.response().setChunked(true);
      }
      event.response()
          .setStatusCode(HttpStatus.HTTP_INTERNAL_ERROR);
      event.next();
    });

    for (Class clazz : getRouteHandlerClass()) {
      RouteHandler routeHandler = AnnotationUtil
          .findMergedAnnotation(clazz, RouteHandler.class);
      if (routeHandler != null) {
        RouteMapping mapping = AnnotatedElementUtils
            .findMergedAnnotation(clazz, RouteMapping.class);
        Router sonRouter;
        if (Objects.nonNull(mapping)) {
          sonRouter = Router.router(vertx);
        } else {
          sonRouter = router;
        }
        Method[] methods = AnnotationUtil
            .getMethodByAnnotation(clazz, RouteMapping.class);
        if (ArrayUtil.isEmpty(methods)) {
          continue;
        }
        Object o = factory.getInstance(clazz);
        for (Method method : methods) {
          ControllerMethodWrap wrap = ControllerMethodWrap.create(method, clazz, o);
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
          List<Route> routes = sonRouter.getRoutes();
          routes.forEach(route -> log.debug(mapping.value() + route.getPath()));
          router.mountSubRouter(mapping.value()[0], sonRouter);
        }
      }
    }
    routerCache.put(vertx, router);
    return router;
  }

  /**
   * 获取拦截器handler
   *
   * @author mrys
   */
  protected void getInterceptorHandler(RoutingContext event) {
    Iterator<Interceptor<RoutingContext, ?>> iterator = interceptors.iterator();
    for (; iterator.hasNext(); ) {
      if (!iterator.next().preHandler(event)) {
        if (event.response().ended()) {
          return;
        }
        event.fail(HttpStatus.HTTP_BAD_REQUEST);
        return;
      }
    }
    if (!event.response().isChunked()) {
      event.response().setChunked(true);
    }
    event.next();
  }

  /**
   * 全局异常处理
   *
   * @author mrys
   */
  protected void getFailureHandler(RoutingContext event) {
    Throwable failure = event.failure();
    log.error(failure.getMessage(), failure);
    event.response()
        .setStatusCode(400)
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8")
//        .end("错误");
        .end(failure.getMessage());
  }

  private Router getFromCache() {
    return routerCache.get(vertx);
  }

  protected Set<Class> getRouteHandlerClass() {
    return classes;
  }


  @Override
  public DefaultRouteFactory addVertx(Vertx vertx) {
    this.vertx = vertx;
    return this;
  }

  /**
   * 添加头拦截器
   *
   * @param interceptor
   * @author mrys
   */
  @Override
  public DefaultRouteFactory addFirst(Interceptor interceptor) {
    interceptors.addFirst(interceptor);
    return this;
  }

  /**
   * 添加最后一个拦截器
   *
   * @param interceptor
   * @author mrys
   */
  @Override
  public DefaultRouteFactory addLast(Interceptor interceptor) {
    interceptors.addLast(interceptor);
    return this;
  }
}
