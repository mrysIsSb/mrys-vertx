package top.mrys.vertx.springboot;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.common.config.ConfigCentreStoreOptions;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.common.launcher.ApplicationContext;
import top.mrys.vertx.common.launcher.MyVerticleFactory;
import top.mrys.vertx.springboot.AutoConfiguration.Red;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Configuration
@EnableConfigurationProperties(Red.class)
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

  @ConfigurationProperties(prefix = "another")
  //todo 刷新
  @Bean
  public Red anotherComponent() {
    return new Red();
  }

  @ConfigurationProperties("acme")
  public static class Red extends ConfigCentreStoreOptions {

  }
}
