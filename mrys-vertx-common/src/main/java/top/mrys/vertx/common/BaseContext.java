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
public interface BaseContext<C extends BaseContext> extends ContextWrapper, Closeable,
//    ReadStream<C>,
    WriteStream<Buffer> {

  @Override
  C exceptionHandler(Handler<Throwable> handler);


/*  @Override
  C handler(Handler<C> handler);

  @Override
  default C pause() {
    return (C) this;
  }


  @Override
  default C resume() {
    return (C) this;
  }

  @Override
  default C fetch(long amount) {
    return (C) this;
  }*/

  @Override
  Future<Void> write(Buffer data);

  @Override
  void write(Buffer data, Handler<AsyncResult<Void>> handler);

  @Override
  default void close(Promise<Void> completion) {
    completion.complete();
  }
}
