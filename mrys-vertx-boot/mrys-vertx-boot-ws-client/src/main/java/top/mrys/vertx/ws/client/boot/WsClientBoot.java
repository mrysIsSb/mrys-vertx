package top.mrys.vertx.ws.client.boot;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import top.mrys.vertx.springboot.http.server.EnableWs;
import top.mrys.vertx.ws.client.boot.controller.TestController;

/**
 * @author mrys
 * @date 2020/10/22
 */

@Slf4j
@SpringBootApplication
@EnableWs
public class WsClientBoot {

  @Value("${name}")
  private String name;

  @Value("#{vertxConfig.getForPath('ws.port',T(java.lang.String))}")
  private String port;

  @Autowired
  private Vertx vertx;

  public static void main(String[] args) {
    ConfigurableApplicationContext run = SpringApplication.run(WsClientBoot.class, args);
    WsClientBoot bean = run.getBean(WsClientBoot.class);
    bean.vertx.setPeriodic(10000, event -> {
      System.out.println(run.getEnvironment().getProperty("ws.port"));
    });
    System.out.println(run.getBean(TestController.class));

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
/*    String CRLF = "\r\n";
    String auth = "*2"+CRLF
        + "$4"+CRLF
        + "auth"+CRLF
        + "$8"+CRLF
        + "123456yj"+CRLF;
    String clientList = "*2"+CRLF
        + "$6"+CRLF
        + "client"+CRLF
        + "$4"+CRLF
        + "list"+CRLF;
    vertx.createNetClient()
        .connect(6380, "192.168.124.16", event -> {
          if (event.succeeded()) {
            NetSocket result = event.result();
            result.handler(event1 -> {
              log.info("redis:{}", event1.toString());
              result.write(clientList);
            });
            result.write(auth, event1 -> {
              if (event1.succeeded()) {
                log.info("成功");
              } else {
                log.error(event1.cause().getMessage(), event1.cause());
              }
            });
          } else {
            log.error(event.cause().getMessage(), event.cause());
          }
        });*/

 /*   vertx.setTimer(10_000, ig -> {
      vertx.createHttpClient().webSocket(80, "localhost", "")
          .onComplete(event -> {
            if (event.succeeded()) {
              WebSocket result = event.result();
              result.write(Buffer.buffer("你好"), event1 -> System.out.println(event1.succeeded()));
              result.closeHandler(event1 -> log.info("client: 关闭"));
              result.handler(event1 -> {
                System.out.println("client:" + event1.toString());
                vertx.setTimer(1000, event2 -> result.write(Buffer.buffer(event1.toString())));
              });
            } else {
              event.cause().printStackTrace();
            }
          });
    });*/
   /* System.out.println(run.getBean("anotherComponent"));
    run.getEnvironment().getPropertySources().forEach(propertySource -> {
      System.out.println("-0-00-0-0-0-0-00000-------------");
      System.out.println(propertySource.getName());
      System.out.println(propertySource.getClass());
    });*/
  }
}
