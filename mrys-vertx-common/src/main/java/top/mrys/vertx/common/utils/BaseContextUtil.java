package top.mrys.vertx.common.utils;

import cn.hutool.core.lang.Assert;
import top.mrys.vertx.common.AbstractProcessContext;
import top.mrys.vertx.common.ContextHandler;
import top.mrys.vertx.common.ProcessContext;

/**
 * @author mrys
 * @date 2021/7/11
 */
public class BaseContextUtil {

  public static <H1 extends ContextHandler> ProcessContext<?, H1, ?> getNextBaseContextByType(
      AbstractProcessContext begin, Class<H1> handlerType) {
    Assert.notNull(handlerType, "handlerType 不能为空");
    AbstractProcessContext ctx = begin.next;
    for (; ; ) {
      if (ctx == null) {
        return null;
      }
      if (handlerType.isAssignableFrom(ctx.handler().getClass())) {
        return ctx;
      }
      ctx = ctx.next;
    }
  }
}
