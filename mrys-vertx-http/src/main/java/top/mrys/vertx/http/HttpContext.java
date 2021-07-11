package top.mrys.vertx.http;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.impl.ContextInternal;
import io.vertx.ext.web.RoutingContext;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import top.mrys.vertx.common.AbstractChannelContext;

/**
 * @author mrys
 * @date 2021/7/6
 */
public class HttpContext extends AbstractChannelContext {

  @Setter
  @Getter
  private RoutingContext routingContext;

  private Handler<Throwable> exceptionHandler;

  private HttpContext(ContextInternal contextInternal) {
    super(contextInternal);
  }

  public static HttpContext create(ContextInternal contextInternal,
      RoutingContext routingContext) {
    HttpContext content = new HttpContext(contextInternal);
    content.routingContext = routingContext;
    return content;
  }

  @Override
  public void close(Promise<Void> completion) {
    Optional.ofNullable(routingContext)
        .ifPresent(context -> {
          if (!context.response().closed()) {
            context.response().close();
          }
        });
  }
}
