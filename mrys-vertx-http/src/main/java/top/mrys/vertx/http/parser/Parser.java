package top.mrys.vertx.http.parser;

/**
 * @author mrys
 * @date 2020/7/9
 */
@FunctionalInterface
public interface Parser<T, U> {

  /**
   * 是否执行
   *
   * @author mrys
   */
  default Boolean canExec(T object) {
    return true;
  }

  void accept(T t, U u);
}
