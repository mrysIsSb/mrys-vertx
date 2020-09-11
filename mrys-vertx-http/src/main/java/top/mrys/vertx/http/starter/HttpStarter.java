package top.mrys.vertx.http.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import sun.instrument.InstrumentationImpl;
import top.mrys.vertx.common.launcher.AbstractStarter;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;
import top.mrys.vertx.common.launcher.Starter;
import top.mrys.vertx.common.launcher.VertxStartedEvent;
import top.mrys.vertx.common.utils.ScanPackageUtil;
import top.mrys.vertx.http.parser.RouteFactory;
import top.mrys.vertx.http.parser.SpringRouteFactoryWarp;

/**
 * @author mrys
 * @date 2020/8/4
 */
@Slf4j
@ComponentScan("top.mrys.vertx.http.parser")
public class HttpStarter extends AbstractStarter<EnableHttp> {

  @Autowired(required = false)
  private Vertx vertx;

  @Autowired
  private MyRefreshableApplicationContext context;

  @Override
  public void start(EnableHttp enableHttp) {
    int port = enableHttp.port();
    if (Objects.isNull(vertx)) {
      log.error("vertx 不能为空null");
      return;
    }
    RouteFactory factory = context.getBean(RouteFactory.class);
    Router router = factory.get();
    Future<String> future = vertx
        .deployVerticle(() -> new httpV(port,router), new DeploymentOptions().setInstances(10));
    future.onSuccess(event -> log.info("http server start port:{}",port));
  }
  @Slf4j
  static class httpV extends AbstractVerticle {
    private int port;
    private Handler<HttpServerRequest> router;

    public httpV(int port, Handler router) {
      this.port = port;
      this.router = router;
    }


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      vertx.createHttpServer().requestHandler(router)
          .listen(port)
          .onSuccess(event -> startPromise.complete())
          .onFailure(startPromise::fail);
    }
  }
}

