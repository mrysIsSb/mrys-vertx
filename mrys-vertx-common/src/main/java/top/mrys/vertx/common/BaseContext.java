package top.mrys.vertx.common;

import io.vertx.core.AsyncResult;
import io.vertx.core.Closeable;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.WriteStream;

/**
 * @author mrys
 * @date 2021/7/6
 */
public interface BaseContext<C extends BaseContext, H extends ContextHandler> extends
    ContextWrapper, Closeable,
    HandleInvoker,
    WriteStream<Buffer> {

  @Override
  C exceptionHandler(Handler<Throwable> handler);

  @Override
  Future<Void> write(Buffer data);

  H handler();

  @Override
  void write(Buffer data, Handler<AsyncResult<Void>> handler);

  @Override
  default void close(Promise<Void> completion) {
    completion.complete();
  }
}
