package top.mrys.vertx.boot.entity;

import lombok.Data;

/**
 * @author mrys
 * @date 2020/10/20
 */
@Data
public class Result<T> {

  private int code;

  private String msg;
  private T data;

  public Result() {
  }

  public static <T> Result<T> ok() {
    Result<T> result = new Result<>();
    result.code = 0;
    return result;
  }

  public static <T> Result<T> okData(T data) {
    Result<T> result = new Result<>();
    result.code = 0;
    result.data = data;
    return result;
  }
}
