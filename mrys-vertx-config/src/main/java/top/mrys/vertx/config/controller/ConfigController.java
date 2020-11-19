package top.mrys.vertx.config.controller;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.common.config.ConfigLoader;
import top.mrys.vertx.http.annotations.GetRoute;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020年11月19日
 */
@RouteHandler
@RouteMapping("/config")
@Slf4j
public class ConfigController {

  @Setter
  private ConfigLoader configLoader;

  @GetRoute({"/getConfig/:serverName/:profile", "/getConfig/:serverName"})
  public Future<JsonObject> getConfig(@PathVar String serverName,
      @PathVar(defValue = "def") String profile) {
    log.debug("/getConfig/{}/{}", serverName, profile);
    return Future.succeededFuture(
        JsonObject.mapFrom(configLoader.getByPath("config." + serverName + "." + profile)));
  }

}
