package top.mrys.vertx.common;

import io.vertx.core.impl.ContextInternal;

/**
 * @author mrys
 * @date 2021/7/11
 */
public class SimpleProcessContext<H extends ContextHandler, C extends ChannelContext> extends
    AbstractProcessContext<SimpleProcessContext, H, C> {

  public SimpleProcessContext(ContextInternal contextInternal, C channelContext, H handler) {
    super(contextInternal, channelContext, handler);
  }

  public static <H extends ContextHandler, C extends ChannelContext> SimpleProcessContext<H, C> create(
      ContextInternal contextInternal, H handler, C channelContext) {
    return new SimpleProcessContext<>(contextInternal, channelContext, handler);
  }
}
