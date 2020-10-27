package top.mrys.vertx.ws.client.boot;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import top.mrys.vertx.springboot.http.server.EnableWs;

/**
 * @author mrys
 * @date 2020/10/22
 */

@Slf4j
@SpringBootApplication
@EnableWs
public class WsClientBoot {

  public static void main(String[] args) {
    ConfigurableApplicationContext run = SpringApplication.run(WsClientBoot.class, args);
    System.out.println(run);
    /*Vertx vertx = Vertx.vertx();

    MyRefreshableApplicationContext context = new MyRefreshableApplicationContext();
    context.register(WsClientBoot.class);
    context.refresh();
    MyVerticleFactory verticleFactory = context.getBean(MyVerticleFactory.class);
    WsVerticle myAbstractVerticle = verticleFactory.getMyAbstractVerticle(WsVerticle.class);
    myAbstractVerticle.setHttpServerOptions(new HttpServerOptions().setPort(8800));
    vertx.deployVerticle(myAbstractVerticle);

    */
    Vertx vertx = Vertx.vertx();
    vertx.setTimer(10_000, ig -> {
      vertx.createHttpClient().webSocket(80, "localhost", "")
          .onComplete(event -> {
            if (event.succeeded()) {
              WebSocket result = event.result();
              result.write(Buffer.buffer("你好"), event1 -> System.out.println(event1.succeeded()));
              result.handler(event1 -> System.out.println("client:" + event1.toString()));
            } else {
              event.cause().printStackTrace();
            }
          });
    });
  }
}
