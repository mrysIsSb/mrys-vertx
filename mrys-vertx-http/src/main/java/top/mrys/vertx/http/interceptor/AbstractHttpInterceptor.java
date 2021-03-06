package top.mrys.vertx.http.interceptor;

import io.vertx.ext.web.RoutingContext;
import org.springframework.core.Ordered;
import top.mrys.vertx.common.utils.Interceptor;

/**
 * @author mrys
 * @date 2020/11/11
 */
public abstract class AbstractHttpInterceptor implements Interceptor<RoutingContext, Object>,
    Ordered {

  @Override
  public int getOrder() {
    return 0;
  }
}
