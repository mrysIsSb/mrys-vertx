package top.mrys.vertx.common;

import io.vertx.core.impl.ContextInternal;
import java.util.Optional;
import top.mrys.vertx.common.utils.BaseContextUtil;

/**
 * @author mrys
 * @date 2021/7/10
 */
public abstract class AbstractProcessContext<C extends ProcessContext, H extends ContextHandler,CC extends ChannelContext> implements
    ProcessContext<C, H,CC> {

  public AbstractProcessContext prev;
  public AbstractProcessContext next;

  private final ContextInternal contextInternal;
  private final CC channelContext;

  private final H handler;

  public AbstractProcessContext(ContextInternal contextInternal, CC channelContext, H handler) {
    this.contextInternal = contextInternal;
    this.channelContext = channelContext;
    this.handler = handler;
  }

  /**
   * 调用下一个
   *
   * @author mrys
   * @param data
   */
  @Override
  public void fireHandle(Object data) {
    Optional.ofNullable(context(this, ContextHandler.class))
        .ifPresent(context -> context.handler().handle(context, data));
  }

  @Override
  public void fireExceptionCaught(Throwable cause) {
    Optional.ofNullable(context(this, ContextHandler.class))
        .ifPresent(context -> context.handler().exceptionCaught(context, cause));
  }

  public <H1 extends ContextHandler> ProcessContext<?, H1,?> context(AbstractProcessContext begin,
      Class<H1> handlerType) {
    return BaseContextUtil.getNextBaseContextByType(begin, handlerType);
  }

  @Override
  public H handler() {
    return this.handler;
  }

  @Override
  public CC getChannelContext() {
    return channelContext;
  }
}
