package top.mrys.vertx.boot.controller;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/9/22
 */
@RouteHandler
@RouteMapping("/config")
public class ConfigController {


  @RouteMapping(value = "/:serverName/:profile")
  public Future<JsonObject> getConfig(@PathVar String serverName, String profile) {
    return Future.succeededFuture(ConfigRepo.getInstance()
        .getForPath(serverName + "." + profile, JsonObject.class, new JsonObject()));
  }

}
