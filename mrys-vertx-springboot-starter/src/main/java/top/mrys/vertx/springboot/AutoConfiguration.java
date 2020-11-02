package top.mrys.vertx.springboot;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import top.mrys.vertx.common.config.ConfigLoader;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.common.factorys.SpringObjectInstanceFactory;
import top.mrys.vertx.common.launcher.ApplicationContext;
import top.mrys.vertx.common.launcher.MyVerticleFactory;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Configuration
public class AutoConfiguration {

  @Autowired
  private ConfigurableApplicationContext context;

  @Bean
  public Vertx vertx() {
    return VertxRelevantObjectInstanceFactory.createVertx();
  }


  /**
   * 对象实例化工厂
   *
   * @author mrys
   */
  @Bean
  public ObjectInstanceFactory objectInstanceFactory() {
    return VertxRelevantObjectInstanceFactory.objectInstanceFactory();
  }

  @Bean
  public ApplicationContext context() {
    return VertxRelevantObjectInstanceFactory.context();
  }

  /**
   * verticle实例化工厂
   *
   * @author mrys
   */
  @Bean
  public MyVerticleFactory myVerticleFactory() {
    MyVerticleFactory verticleFactory = new MyVerticleFactory();
    verticleFactory.setInstanceFactory(objectInstanceFactory());
    verticleFactory.setContext(context());
    return verticleFactory;
  }

  @Bean
  public ConfigRepo vertxConfig() {
    return VertxRelevantObjectInstanceFactory.getConfigLoader().load();
  }

}
