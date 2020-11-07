package top.mrys.vertx.boot.controller;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import top.mrys.vertx.common.config.ConfigLoader;
import top.mrys.vertx.http.annotations.GetRoute;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.annotations.PostRoute;
import top.mrys.vertx.http.annotations.ReqBody;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/9/22
 */
@RouteHandler
@RouteMapping("/config")
public class ConfigController {


  @GetRoute("/getConfig/:serverName/:profile")
  public Future<JsonObject> getConfig(@PathVar String serverName, @PathVar String profile) {
    Object o = new ConfigLoader().getByPath("config." + serverName + "." + profile);
    return Future.succeededFuture(JsonObject.mapFrom(o));
  }

  @PostRoute
  public Future<JsonObject> save(@ReqBody JsonObject user) {
    return Future.succeededFuture(user);
  }

}
