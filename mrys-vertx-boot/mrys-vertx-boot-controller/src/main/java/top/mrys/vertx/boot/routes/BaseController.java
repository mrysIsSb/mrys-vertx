package top.mrys.vertx.boot.routes;

import io.vertx.core.Future;
import top.mrys.vertx.springboot.http.server.annotations.GetRoute;

/**
 * @author mrys
 * @date 2021/1/13
 */
public interface BaseController {

  @GetRoute("/info")
  default Future<String> info() {
    return Future.succeededFuture("info");
  }

}
