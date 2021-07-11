package top.mrys.vertx.common;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mrys
 * @date 2021/7/11
 */
@Slf4j
public abstract class AbstractContextHandler implements ContextHandler {

  @Override
  public void handlerAdded(ProcessContext context) {
    log.info("add handler:{}", context.handler());
  }

  @Override
  public void handlerRemoved(ProcessContext context) {
    log.info("remove handler:{}", context.handler());
  }

  @Override
  public void handle(ProcessContext context, Object data) {
    context.fireHandle(data);
  }

  @Override
  public void exceptionCaught(ProcessContext context, Throwable cause) {
    context.fireExceptionCaught(cause);
  }
}
