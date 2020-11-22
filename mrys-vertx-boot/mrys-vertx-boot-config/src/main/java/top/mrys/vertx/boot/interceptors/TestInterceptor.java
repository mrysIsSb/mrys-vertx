package top.mrys.vertx.boot.interceptors;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.utils.Interceptor;
import top.mrys.vertx.http.interceptor.AbstractHttpInterceptor;

/**
 * @author mrys
 * @date 2020/11/11
 */
@Slf4j
@Component
public class TestInterceptor extends AbstractHttpInterceptor {

  @Override
  public boolean preHandler(RoutingContext context) {
    log.info("{}", context.request().path());
    return true;
  }
}
