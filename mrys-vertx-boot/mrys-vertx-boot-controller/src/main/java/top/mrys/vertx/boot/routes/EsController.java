package top.mrys.vertx.boot.routes;

import cn.hutool.json.JSONObject;
import io.vertx.core.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.boot.api.EsApi;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.annotations.ReqBody;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.springboot.http.server.annotations.GetRoute;
import top.mrys.vertx.springboot.http.server.annotations.PutRoute;

/**
 * @author mrys
 * @date 2020/11/23
 */
@RouteHandler
@RouteMapping("/es")
@Slf4j
public class EsController {

  @Autowired
  private EsApi esApi;

  @GetRoute
  public Future<JSONObject> info() {
    return esApi.info();
  }

  @PutRoute({"/:index/:type/:id", "/:index/:type"})
  public Future<JSONObject> putData(@PathVar String index, @PathVar String type,
      @PathVar(required = false) String id,
      @ReqBody JSONObject data) {
    log.info("{}", data);
    return esApi.putData(index, type, id, data);
  }
}
