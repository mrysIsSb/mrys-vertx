package top.mrys.vertx.boot.interceptors;

import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.utils.Interceptor;

/**
 * @author mrys
 * @date 2020/11/11
 */
@Slf4j
public class TestInterceptor implements Interceptor<RoutingContext, Object> {

  @Override
  public boolean preHandler(RoutingContext context) {
    log.info("{}", context.request().path());
    return true;
  }
}
