package top.mrys.vertx.http.starter;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import java.util.Set;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.consts.ConstLog;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.http.parser.RouteFactory;

/**
 * @author mrys
 * @date 2020/9/12
 */
@Slf4j
public class HttpVerticle extends MyAbstractVerticle {

  @Getter
  @Setter
  private Supplier<Integer> port;
  @Getter
  @Setter
  private Supplier<Set<Class>> routeClassProvider;


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    RouteFactory routeFactory = context.getInstanceFactory().getInstance(RouteFactory.class);
    routeFactory.addObjectInstanceFactory(context.getInstanceFactory());
    routeFactory.addClasses(routeClassProvider.get());
    routeFactory.addVertx(vertx);
    Router router = routeFactory.get();

    HttpServerOptions options = new HttpServerOptions();
    options.setLogActivity(true);
    vertx.createHttpServer(options)
        .requestHandler(router)
        .listen(port.get())
        .onSuccess(event -> startPromise.complete())
        .onFailure(startPromise::fail);
  }

  @Override
  public void stop() throws Exception {
    log.info(ConstLog.log_template1, "http stop");
  }
}
