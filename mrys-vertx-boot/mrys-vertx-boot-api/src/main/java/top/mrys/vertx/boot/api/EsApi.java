package top.mrys.vertx.boot.api;

import cn.hutool.json.JSONObject;
import io.vertx.core.Future;
import top.mrys.vertx.eventbus.MicroClient;
import top.mrys.vertx.eventbus.MicroClient.ConfigProcess;
import top.mrys.vertx.eventbus.proxy.WebClientProcess;
import top.mrys.vertx.springboot.http.server.annotations.GetRoute;

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
}
