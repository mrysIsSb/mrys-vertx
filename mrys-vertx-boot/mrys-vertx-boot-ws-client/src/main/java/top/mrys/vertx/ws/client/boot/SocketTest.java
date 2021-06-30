package top.mrys.vertx.ws.client.boot;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author mrys
 * @date 2021/3/6
 */
public class SocketTest {

  public static void main(String[] args) {
    LocalDate date = LocalDate.parse("2010-12-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    System.out.println(date);
    Vertx.vertx().createNetClient().connect(8080, "localhost", event -> {
      if (event.succeeded()) {
        event.result().handler(event1 -> {
          System.out.println(event1.toString());
          event.result().write("hello");
        });
        event.result().write("hello");
//        sayHello(event.result());
        System.out.println("success");
      }else {
        System.out.println(event.cause());
      }
    });

/*    Vertx.vertx().createNetServer()
        .connectHandler(event -> {
          System.out.println(event.remoteAddress().hostAddress());
          event.handler(event1 -> {
            System.out.println(event1.toString());
            event.write("hello:"+event1.toString());
          });
          event.write("hello:"+event.remoteAddress().hostAddress());
        })
        .listen(9099);*/

  }

  public static void sayHello(NetSocket socket){
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("success");
    socket.write("hello").onSuccess(event -> sayHello(socket));
  }

}
