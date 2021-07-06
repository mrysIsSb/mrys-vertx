package top.mrys.vertx.http;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;
import io.vertx.ext.web.RoutingContext;
import java.util.Optional;
import top.mrys.vertx.common.BaseContent;

/**
 * @author mrys
 * @date 2021/7/6
 */
public class HttpContent implements BaseContent<HttpContent> {

  private ContextInternal contextInternal;

  private RoutingContext routingContext;

  private HttpContent() {
  }

  public HttpContent create(ContextInternal contextInternal,
      RoutingContext routingContext) {
    HttpContent content = new HttpContent();
    content.contextInternal = contextInternal;
    content.routingContext = routingContext;
    return content;
  }

  @Override
  public ContextInternal getContext() {
    return contextInternal;
  }


  @Override
  public HttpContent exceptionHandler(Handler<Throwable> handler) {
// TODO: 2021/7/6
    return null;
  }


  @Override
  public HttpContent handler(Handler<Buffer> handler) {
    // TODO: 2021/7/6  
    return this;
  }


  @Override
  public ReadStream<Buffer> pause() {
    // TODO: 2021/7/6  
    return null;
  }


  @Override
  public ReadStream<Buffer> resume() {
    // TODO: 2021/7/6  
    return null;
  }


  @Override
  public ReadStream<Buffer> fetch(long amount) {
    return null;
  }


  @Override
  public ReadStream<Buffer> endHandler(Handler<Void> endHandler) {
    return null;
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
