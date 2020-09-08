package top.mrys.vertx.boot.routes;

import cn.hutool.core.convert.Convert;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.boot.api.SysUserApi;
import top.mrys.vertx.common.manager.VertxManager;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteHeader;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * @author mrys
 * @date 2020/7/27
 */
@RouteHandler
@RouteMapping("/demo")
public class DemoController {

  @Autowired
  private SysUserApi sysUserApi;

  @Autowired
  private VertxManager vertxManager;

  @RouteMapping(value = "/test1", method = EnumHttpMethod.GET)
  public Handler<RoutingContext> test1() {
    return event -> {
      String id = event.request().getParam("id");
      sysUserApi.getById(Convert.toInt(id))
          .onSuccess(sysUser -> event.response().end(JsonObject.mapFrom(sysUser).toString()))
          .onFailure(event::fail);
    };
  }

/*  @RouteHeader(name = "headerName", value = "value123123")
  @RouteHeader(name = "headerName", value = "value123123")
  @RouteMapping(value = "/test2", method = EnumHttpMethod.GET)
  //不推荐
  public Map test2() {
    Map map = new HashMap<>();
    return map;
  }*/
/*
  @RouteMapping(value = "/test3", method = EnumHttpMethod.GET)
  public Future<DeploymentData> test3() {
    Promise<DeploymentData> promise = Promise.promise();
    promise.complete(new DeploymentData().setDeployId("123234324"));
    return promise.future();
  }*/

}