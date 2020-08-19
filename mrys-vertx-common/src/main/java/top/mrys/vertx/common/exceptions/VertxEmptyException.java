package top.mrys.vertx.common.exceptions;

/**
 * @author mrys
 * @date 2020/8/19
 */
@NoLog
public class VertxEmptyException extends MrysException{
  public VertxEmptyException() {
    super(EnumException.EMPTY_VERTX);
  }
  public static void doThrow() throws VertxEmptyException{
    throw new VertxEmptyException().removeStackTraceElement();
  }
}
