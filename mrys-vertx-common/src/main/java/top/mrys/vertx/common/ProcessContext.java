package top.mrys.vertx.common;

import io.vertx.core.Closeable;
import io.vertx.core.Promise;

/**
 * @author mrys
 * @date 2021/7/6
 */
public interface ProcessContext<C extends ProcessContext, H extends ContextHandler, CC extends ChannelContext> extends
    Closeable,
    HandleInvoker {

  CC getChannelContext();

  H handler();

  @Override
  default void close(Promise<Void> completion) {
    completion.complete();
  }
}
