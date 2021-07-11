package top.mrys.vertx.common;

/**
 * @author mrys
 * @date 2021/7/10
 */
public interface ContextHandler {

  void handlerAdded(ProcessContext context);

  void handlerRemoved(ProcessContext context);

  void handle(ProcessContext context, Object data);

  void exceptionCaught(ProcessContext context, Throwable cause);

}
