package top.mrys.vertx.common;

/**
 * @author mrys
 * @date 2021/7/10
 */
public interface ContextHandler {

  void handlerAdded(BaseContext ctx);

  void handlerRemoved(BaseContext ctx);

  void handle(BaseContext context);

  void exceptionCaught(BaseContext ctx, Throwable cause);

}
