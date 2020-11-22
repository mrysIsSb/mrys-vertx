package top.mrys.vertx.config.starter;

import cn.hutool.core.collection.CollectionUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.VertxOptions;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.common.launcher.ApplicationContext;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.config.controller.ConfigController;
import top.mrys.vertx.http.starter.HttpVerticle;

/**
 * @author mrys
 * @date 2020/11/19
 */
@Slf4j
public class ConfigVerticle extends MyAbstractVerticle {

  //开启http
  public static final int enableHttp = 1 << 0;

  @Getter
  @Setter
  private Supplier<Integer> enableService = () -> 0;
  /**
   * 获取http 端口
   *
   * @author mrys
   */
  @Getter
  @Setter
  private Supplier<Integer> httpPort;


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    if ((enableService.get() & enableHttp) != 0) {
      startHttp();
    }
    startPromise.complete();
  }

  protected void startHttp() {
    vertx.deployVerticle(() -> context.getVerticleFactory()
            .getMyAbstractVerticle(HttpVerticle.class, httpVerticle -> {
              ObjectInstanceFactory instanceFactory = httpVerticle.getContext()
                  .getInstanceFactory();
              ApplicationContext clone = httpVerticle.getContext().clone();
              clone.setInstanceFactory(
                  new ObjectInstanceFactory() {
                    @Override
                    public <T> T getInstance(Class<T> clazz) {
                      if (ConfigController.class.equals(clazz)) {
                        ConfigController controller = new ConfigController();
                        controller.setConfigLoader(context.getConfigLoader());
                        return (T) controller;
                      }
                      return instanceFactory.getInstance(clazz);
//                      throw new NullPointerException("不能实例化该类:"+clazz.getName());
                    }
                  });
              httpVerticle.setContext(clone);
              httpVerticle.setPort(httpPort);
              httpVerticle
                  .setRouteClassProvider(
                      () -> CollectionUtil.set(Boolean.FALSE, ConfigController.class));
              return httpVerticle;
            })
        , new DeploymentOptions().setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE), re -> {
          if (re.failed()) {
            log.error(re.cause().getMessage(), re.cause());
          }
        });
  }
}
