package top.mrys.vertx.http;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.streams.WriteStream;
import io.vertx.ext.web.RoutingContext;
import java.util.Optional;
import top.mrys.vertx.common.AbstractBaseContext;
import top.mrys.vertx.common.ContextHandler;

/**
 * @author mrys
 * @date 2021/7/6
 */
public class HttpContext extends AbstractBaseContext<HttpContext, ContextHandler> {

  private ContextInternal contextInternal;

  private RoutingContext routingContext;

  private Handler<Throwable> exceptionHandler;

  private HttpContext() {
  }

  public HttpContext create(ContextInternal contextInternal,
      RoutingContext routingContext) {
    HttpContext content = new HttpContext();
    content.contextInternal = contextInternal;
    content.routingContext = routingContext;
    return content;
  }

  @Override
  public ContextInternal getContext() {
    return contextInternal;
  }


  @Override
  public ContextHandler handler() {
    return null;
  }

  @Override
  public HttpContext exceptionHandler(Handler<Throwable> handler) {
    this.exceptionHandler = handler;
    return this;
  }


  @Override
  public Future<Void> write(Buffer data) {
    return routingContext.response().end(data);
  }


  @Override
  public void write(Buffer data, Handler<AsyncResult<Void>> handler) {
    write(data).onComplete(handler);
  }


  @Override
  public void end(Handler<AsyncResult<Void>> handler) {

  }


  @Override
  public WriteStream<Buffer> setWriteQueueMaxSize(int maxSize) {
    return null;
  }


  @Override
  public boolean writeQueueFull() {
    return false;
  }


  @Override
  public WriteStream<Buffer> drainHandler(Handler<Void> handler) {
    return null;
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
