package top.mrys.vertx.common;

import java.util.Optional;
import top.mrys.vertx.common.utils.BaseContextUtil;

/**
 * @author mrys
 * @date 2021/7/10
 */
public abstract class AbstractBaseContext<C extends BaseContext, H extends ContextHandler> implements
    BaseContext<C, H> {

  public AbstractBaseContext prev;
  public AbstractBaseContext next;

  /**
   * 调用下一个
   *
   * @author mrys
   */
  @Override
  public void fireHandle() {
    Optional.ofNullable(context(this, ContextHandler.class))
        .ifPresent(context -> context.handler().handle(context));
  }

  @Override
  public void fireExceptionCaught(Throwable cause) {
    Optional.ofNullable(context(this, ContextHandler.class))
        .ifPresent(context -> context.handler().exceptionCaught(context, cause));
  }

  public <H1 extends ContextHandler> BaseContext<?, H1> context(AbstractBaseContext begin,
      Class<H1> handlerType) {
    return BaseContextUtil.getNextBaseContextByType(begin, handlerType);
  }

}
