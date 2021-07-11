package top.mrys.vertx.common;

/**
 * @author mrys
 * @date 2021/7/10
 */
public interface HandleInvoker {

  void fireHandle(Object data);

  void fireExceptionCaught(Throwable cause);
}
