package top.mrys.vertx.common;

import cn.hutool.core.lang.Assert;
import io.netty.util.internal.ObjectUtil;
import top.mrys.vertx.common.utils.BaseContextUtil;

/**
 * @author mrys
 * @date 2021/7/10
 */
public abstract class AbstractDataPipeline implements DataPipeline {

  private AbstractBaseContext head;
  private AbstractBaseContext tail;


  @Override
  public final <H1 extends ContextHandler> BaseContext<?, H1> context(Class<H1> handlerType) {
    return BaseContextUtil.getNextBaseContextByType(head, handlerType);
  }
}
