package top.mrys.vertx.springboot.micro;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.eventbus.proxy.HttpClientProxyFactory;

/**
 * @author mrys
 * @date 2020/11/14
 */
@Configuration
public class MicroAutoConfiguration {

  @Bean
  public HttpClientProxyFactory httpClientProxyFactory(ObjectInstanceFactory instanceFactory) {
    HttpClientProxyFactory factory = new HttpClientProxyFactory();
    factory.setObjectInstanceFactory(instanceFactory);
    return factory;
  }
}
