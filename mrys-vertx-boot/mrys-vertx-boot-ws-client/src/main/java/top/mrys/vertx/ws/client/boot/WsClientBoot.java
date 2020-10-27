package top.mrys.vertx.ws.client.boot;

import io.vertx.core.Vertx;

/**
 * @author mrys
 * @date 2020/10/22
 */
public class WsClientBoot {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.createHttpClient().webSocket(8800,"localhost","")
        .onComplete(event -> {
          if (event.succeeded()) {
            event.result().handler(event1 -> System.out.println(event1.toString()));
            System.out.println("成功");
          }else {
            event.cause().printStackTrace();
          }
        });
  }

}
