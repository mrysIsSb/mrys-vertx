package top.mrys.vertx.demo;

import io.vertx.core.AbstractVerticle;

/**
 * @author mrys
 * 2021/6/28
 */
public class MainVerticle extends AbstractVerticle {
  /**
   * If your verticle does a simple, synchronous start-up then override this method and put your start-up
   * code in here.
   *
   * @throws Exception
   */
  @Override
  public void start() throws Exception {
    System.out.println("hello");
  }
}
