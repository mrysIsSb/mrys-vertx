package top.mrys.vertx.http.exceptions;

/**
 * @author mrys
 * @date 2020/9/26
 */
public class HeaderVarRequiredException extends RuntimeException {


  public HeaderVarRequiredException() {
    super("headerVar 未获取到数据");
  }
}
