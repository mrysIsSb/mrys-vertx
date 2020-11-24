package top.mrys.vertx.boot.api;

import cn.hutool.json.JSONObject;
import io.vertx.core.Future;
import top.mrys.vertx.eventbus.MicroClient;
import top.mrys.vertx.eventbus.MicroClient.ConfigProcess;
import top.mrys.vertx.eventbus.proxy.WebClientProcess;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.annotations.ReqBody;
import top.mrys.vertx.springboot.http.server.annotations.GetRoute;
import top.mrys.vertx.springboot.http.server.annotations.PostRoute;
import top.mrys.vertx.springboot.http.server.annotations.PutRoute;

/**
 * @author mrys
 * @date 2020/11/23
 */
@MicroClient(ConfigProcess = {
    @ConfigProcess(processClass = WebClientProcess.class, args = {"=es"})
})
public interface EsApi {

  @GetRoute("/")
  Future<JSONObject> info();

  @PutRoute("/:index/:type/:id")
  Future<JSONObject> putData(@PathVar String index, @PathVar String type,
      @PathVar String id,
      @ReqBody JSONObject data);

  @PostRoute("/:index/:type/")
  Future<JSONObject> putDataAutoId(@PathVar String index, @PathVar String type,
      @ReqBody JSONObject data);

  @GetRoute("/:index/:type/:id/_source")
  Future<JSONObject> getDataSource(@PathVar String index, @PathVar String type,
      @PathVar String id);
}
