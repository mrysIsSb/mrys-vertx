package top.mrys.vertx.eventbus;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mrys
 * @date 2020/10/20
 */
@Configuration
public class MicroClientConfig {

  @Autowired
  private Vertx vertx;

  @Bean
  public WebClient webClient() {
    return WebClient.create(vertx, new WebClientOptions().setKeepAlive(true));
  }


}
