package top.mrys.vertx.boot.routes;

import cn.hutool.json.JSONObject;
import io.vertx.core.Future;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.boot.api.EsApi;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.springboot.http.server.annotations.GetRoute;

/**
 * @author mrys
 * @date 2020/11/23
 */
@RouteHandler
@RouteMapping("/es")
public class EsController {

  @Autowired
  private EsApi esApi;

  @GetRoute
  public Future<JSONObject> info() {
    return esApi.info();
  }

}
