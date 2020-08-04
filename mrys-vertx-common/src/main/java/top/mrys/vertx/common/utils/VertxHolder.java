package top.mrys.vertx.common.utils;

import io.vertx.core.Vertx;

/**
 * @author mrys
 * @date 2020/8/4
 */
public class VertxHolder {
  private static Vertx mainVertx;

  public static Vertx getMainVertx() {
    return mainVertx;
  }

  public static void setMainVertx(Vertx mainVertx) {
    VertxHolder.mainVertx = mainVertx;
  }
}
