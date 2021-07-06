package top.mrys.vertx.common;

import io.vertx.core.Closeable;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.streams.ReadStream;
import io.vertx.core.streams.WriteStream;

/**
 * @author mrys
 * @date 2021/7/6
 */
public interface BaseContent<C extends BaseContent> extends ContextWrapper, Closeable,
    ReadStream<Buffer>,
    WriteStream<Buffer> {

  @Override
  C exceptionHandler(Handler<Throwable> handler);


  @Override
  C handler(Handler<Buffer> handler);

  @Override
  default void close(Promise<Void> completion) {
    completion.complete();
  }
}
