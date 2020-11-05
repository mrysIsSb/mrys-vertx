package top.mrys.vertx.springboot.http.server;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import top.mrys.vertx.http.starter.HttpVerticle;

/**
 * @author mrys
 * @date 2020/11/3
 */

public class HttpAutoConfiguration {

  @Bean
  public SpringRouteFactoryWarp routeFactoryWarp() {
    return new SpringRouteFactoryWarp();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public HttpVerticle httpVerticle() {
    return new HttpVerticle();
  }

  @Bean
  public BeanFactoryAwareBean beanFactoryAwareBean() {
    return new BeanFactoryAwareBean();
  }
}
