package top.mrys.vertx.ws.client.boot.controller;

import io.vertx.core.Future;
import top.mrys.vertx.http.annotations.GetRoute;
import top.mrys.vertx.http.annotations.ReqParam;
import top.mrys.vertx.http.annotations.RouteHandler;

/**
 * @author mrys
 * @date 2020/11/3
 */
@RouteHandler
public class TestController {

  @GetRoute
  public Future<String> test(@ReqParam String name) {
    return Future.succeededFuture("hello：" + name);
  }
}
