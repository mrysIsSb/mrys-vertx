package top.mrys.vertx.boot.controller;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * @author mrys
 * @date 2020/9/22
 */
@RouteHandler
@RouteMapping("/config")
public class ConfigController {


  @RouteMapping(method = EnumHttpMethod.GET,value = "/getConfig/:serverName/:profile")
  public Future<JsonObject> getConfig(@PathVar String serverName,@PathVar String profile) {
    return Future.succeededFuture(ConfigRepo.getInstance()
        .getForPath(serverName + "." + profile, JsonObject.class, new JsonObject()));
  }

  @RouteMapping(method = EnumHttpMethod.GET,value = "/getAll")
  public Future<JsonObject> getAll() {
    return Future.succeededFuture(ConfigRepo.getInstance().getData());
  }

}
