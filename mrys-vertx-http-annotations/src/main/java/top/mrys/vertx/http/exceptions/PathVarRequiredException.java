package top.mrys.vertx.http.exceptions;

/**
 * @author mrys
 * @date 2020/9/26
 */
public class PathVarRequiredException extends RuntimeException {


  public PathVarRequiredException() {
    super("pathVar 未获取到数据");
  }
}
