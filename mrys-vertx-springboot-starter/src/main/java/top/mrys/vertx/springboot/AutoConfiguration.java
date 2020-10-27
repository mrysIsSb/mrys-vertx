package top.mrys.vertx.springboot;

import io.vertx.core.Vertx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.common.factorys.SpringObjectInstanceFactory;
import top.mrys.vertx.common.launcher.MyVerticleFactory;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Configuration
public class AutoConfiguration {

  @Bean
  public Vertx vertx() {
    return Vertx.vertx();
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

  /**
   * verticle实例化工厂
   *
   * @author mrys
   */
  @Bean
  public MyVerticleFactory myVerticleFactory(ObjectInstanceFactory factory) {
    MyVerticleFactory verticleFactory = new MyVerticleFactory();
    verticleFactory.setInstanceFactory(factory);
    return verticleFactory;
  }

}
