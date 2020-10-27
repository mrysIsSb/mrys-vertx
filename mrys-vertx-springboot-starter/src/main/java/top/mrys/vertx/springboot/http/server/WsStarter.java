package top.mrys.vertx.springboot.http.server;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import top.mrys.vertx.common.launcher.MyVerticleFactory;
import top.mrys.vertx.http.starter.WsVerticle;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Slf4j
public class WsStarter implements ApplicationListener<ApplicationStartedEvent> {

  @Autowired
  private Vertx vertx;
  @Autowired
  private MyVerticleFactory myVerticleFactory;

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    vertx.deployVerticle(() -> myVerticleFactory.getMyAbstractVerticle(WsVerticle.class),
        new DeploymentOptions()
            .setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE), re -> {
          if (re.succeeded()) {
            log.info("wsVerticle 启动成功");
          } else {
            log.error(re.cause().getMessage(), re.cause());
          }
        });
  }
}
