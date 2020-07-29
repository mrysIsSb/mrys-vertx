package top.mrys.vertx.boot;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import top.mrys.vertx.common.launcher.MyLauncher;

/**
 * @author mrys
 * @date 2020/7/21
 */
public class Boot {

  public static void main(String[] args) {
    Vertx run = MyLauncher.run(Boot.class, args);
  /*  run.createHttpServer().requestHandler(event -> {
      event.response().end()
    }).listen()*/
//    CompositeFuture.all().
  }
}
