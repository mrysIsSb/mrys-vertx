package top.mrys.vertx.springboot.micro;

import io.vertx.core.http.HttpClient;
import io.vertx.ext.web.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.eventbus.proxy.HttpClientProxyFactory;
import top.mrys.vertx.eventbus.proxy.WebClientProcess;

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

  @Bean
//  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public WebClient webClient(HttpClient httpClient) {
    return WebClient.wrap(httpClient);
  }

  @Bean
  public WebClientProcess webClientProcess() {
    return new WebClientProcess();
  }
}
