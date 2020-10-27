package top.mrys.vertx.springboot.http.server;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.http.starter.WsVerticle;

/**
 * ws 自动配置
 *
 * @author mrys
 * @date 2020/10/27
 */
@Configuration
public class WsAutoConfiguration {

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public WsVerticle wsVerticle(ObjectInstanceFactory instanceFactory) {
    WsVerticle wsVerticle = new WsVerticle();
    wsVerticle.setInstanceFactory(instanceFactory);
    return wsVerticle;
  }
}
