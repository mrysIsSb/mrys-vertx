package top.mrys.vertx.http.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxImpl;
import io.vertx.ext.web.Router;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.launcher.AbstractStarter;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;
import top.mrys.vertx.http.parser.RouteFactory;

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
  public void start() {
    int port = ConfigRepo.getInstance()
        .getForPath(a.configPrefix() + ".port", Integer.class, a.port());
    if (Objects.isNull(vertx)) {
      log.error("vertx 不能为空null");
      return;
    }
    RouteFactory factory = context.getBean(RouteFactory.class);
    Router router = factory.get();
    Future<String> future = vertx
        .deployVerticle(() -> new HttpVerticle(port, router),
            new DeploymentOptions().setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE));
    future.onSuccess(event -> log.info("http server started port:{}", port));
  }
}

