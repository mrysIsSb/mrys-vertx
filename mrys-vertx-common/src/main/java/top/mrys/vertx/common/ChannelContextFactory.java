package top.mrys.vertx.common;

import io.vertx.core.impl.ContextInternal;

/**
 * @author mrys
 * @date 2021/7/11
 */
public interface ChannelContextFactory<T extends ChannelContext> {

  T get(ContextInternal contextInternal);
}
