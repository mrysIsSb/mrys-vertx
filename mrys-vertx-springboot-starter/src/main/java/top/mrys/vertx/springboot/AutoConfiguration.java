package top.mrys.vertx.springboot;

import io.vertx.core.Vertx;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
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
    Vertx vertx = Vertx.vertx();
    addShutdownHook(vertx);
    return vertx;
  }

  private void addShutdownHook(Vertx vertx) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      CountDownLatch latch = new CountDownLatch(1);
      vertx.close().onComplete(event -> latch.countDown());
      try {
        System.out.println("vertx ---- stop");
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }));
  }
  /**
   * 对象实例化工厂
   *
   * @author mrys
   */
  @Bean
  public ObjectInstanceFactory objectInstanceFactory() {
    return new SpringObjectInstanceFactory();
  }

  @Bean
  public ApplicationContext context() {
    ApplicationContext applicationContext = new ApplicationContext();
    applicationContext.setInstanceFactory(objectInstanceFactory());
    return applicationContext;
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

}
