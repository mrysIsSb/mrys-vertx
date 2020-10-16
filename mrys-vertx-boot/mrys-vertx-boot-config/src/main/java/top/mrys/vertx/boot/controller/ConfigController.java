package top.mrys.vertx.boot.controller;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.function.Function;
import top.mrys.vertx.boot.SysUser;
import top.mrys.vertx.common.config.ConfigRepo;
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
    return Future.succeededFuture(ConfigRepo.getInstance()
        .getForPath(serverName + "." + profile, JsonObject.class, new JsonObject()));
  }

  @GetRoute
  public Future<JsonObject> getAll() {
    return Future.succeededFuture(ConfigRepo.getInstance().getData());
  }

  @PostRoute
  public Future<Boolean> save(@ReqBody SysUser user) {
    return Future.succeededFuture(true);
  }

}
