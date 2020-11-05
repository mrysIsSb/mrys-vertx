package top.mrys.vertx.ws.client.boot.manager;

import io.vertx.core.Future;
import top.mrys.vertx.http.annotations.GetRoute;
import top.mrys.vertx.http.annotations.ReqParam;
import top.mrys.vertx.http.annotations.RouteHandler;

/**
 * @author mrys
 * @date 2020/11/5
 */
@RouteHandler
public class TestController1 {

  @GetRoute
  public Future<String> test(@ReqParam String name) {
    return Future.succeededFuture("sb：" + name);
  }

}
