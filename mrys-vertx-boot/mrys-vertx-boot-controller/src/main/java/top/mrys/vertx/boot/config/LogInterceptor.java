package top.mrys.vertx.boot.config;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mrys.vertx.http.interceptor.AbstractHttpInterceptor;

/**
 * @author mrys
 * @date 2021/1/13
 */
@Component
@Slf4j
public class LogInterceptor extends AbstractHttpInterceptor {

  @Override
  public boolean preHandler(RoutingContext context) {
    log.info("{}", context.request().path());
    return true;
  }
}
