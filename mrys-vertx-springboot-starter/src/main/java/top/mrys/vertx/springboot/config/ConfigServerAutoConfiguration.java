package top.mrys.vertx.springboot.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import top.mrys.vertx.config.starter.ConfigVerticle;
import top.mrys.vertx.http.starter.HttpVerticle;

/**
 * @author mrys
 * @date 2020/11/20
 */

public class ConfigServerAutoConfiguration {



  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public ConfigVerticle configVerticle() {
    return new ConfigVerticle();
  }
}
