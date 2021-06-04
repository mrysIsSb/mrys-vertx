package top.mrys.vertx.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
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
  private HttpServerOptions serverOptions = new HttpServerOptions();

  @Getter
  @Setter
  private Integer enableService = 0;


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    if ((enableService & enableHttp) != 0) {
      startHttp();
    }
    startPromise.complete();
  }

  protected void startHttp() {
    vertx.deployVerticle(() -> {
          HttpVerticle bean = context.getInstance(HttpVerticle.class);
          bean.setServerOptions(serverOptions);
          bean.setRouteClass(CollectionUtil.set(Boolean.FALSE, ConfigController.class));
          return bean;
        }, new DeploymentOptions().setConfig(config())
            .setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE)/*todo 自定义*/,
        re -> {
          if (re.succeeded()) {
            log.info("http server started port:{}", serverOptions.getPort());
          } else {
            log.error(re.cause().getMessage(), re.cause());
          }
        });
    /*vertx.deployVerticle(() -> context.getVerticleFactory()
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
//              httpVerticle.setPort(httpPort);
              httpVerticle
                  .setRouteClass(CollectionUtil.set(Boolean.FALSE, ConfigController.class));
              return httpVerticle;
            })
        , new DeploymentOptions().setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE), re -> {
          if (re.failed()) {
            log.error(re.cause().getMessage(), re.cause());
          }
        });*/
  }
}
