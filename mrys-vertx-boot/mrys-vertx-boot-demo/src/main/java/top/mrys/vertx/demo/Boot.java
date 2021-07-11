package top.mrys.vertx.demo;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.mrys.vertx.demo.controller.HelloController;
import top.mrys.vertx.http.HttpChannelContextFactory;

/**
 * @author mrys 2021/6/28
 */
@Slf4j
@SpringBootApplication
public class Boot {

  public static void main(String[] args) {
//    SpringApplication.run(Boot.class, args);
    Vertx vertx = Vertx.vertx();
    HttpChannelContextFactory factory = new HttpChannelContextFactory();
    factory.getControllers().add(new HelloController());
    vertx.createHttpServer()
        .requestHandler(factory.init(vertx))
        .listen(8888);
  }
}
