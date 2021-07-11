package top.mrys.vertx.common.utils;

import cn.hutool.core.lang.Assert;
import top.mrys.vertx.common.AbstractBaseContext;
import top.mrys.vertx.common.BaseContext;
import top.mrys.vertx.common.ContextHandler;

/**
 * @author mrys
 * @date 2021/7/11
 */
public class BaseContextUtil {

  public static <H1 extends ContextHandler> BaseContext<?, H1> getNextBaseContextByType(
      AbstractBaseContext begin, Class<H1> handlerType) {
    Assert.notNull(handlerType, "handlerType 不能为空");
    AbstractBaseContext ctx = begin.next;
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
