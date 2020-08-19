package top.mrys.vertx.boot.controller;

import cn.hutool.core.convert.Convert;
import io.vertx.config.spi.utils.JsonObjectHelper;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.Deployment;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.boot.dao.mysql.SysUserMapper;
import top.mrys.vertx.boot.service.SysUserService;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.common.manager.VertxManager;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteHeader;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;
import top.mrys.vertx.boot.entity.SysUser;

/**
 * @author mrys
 * @date 2020/7/27
 */
@RouteHandler
@RouteMapping("/demo")
public class DemoController {

  @Autowired
  private SysUserService sysUserService;

  @Autowired
  private VertxManager vertxManager;

  @RouteMapping(value = "/test1",method = EnumHttpMethod.GET)
  public Handler<RoutingContext> test1() {
    return event -> {
      String id = event.request().getParam("id");
      sysUserService.getById(Convert.toInt(id))
          .onSuccess(sysUser -> event.response().end(JsonObject.mapFrom(sysUser).toString()))
      .onFailure(event::fail);
    };
  }

  @RouteHeader(name="headerName",value = "value123123")
  @RouteHeader(name="headerName",value = "value123123")
  @RouteMapping(value = "/test2",method = EnumHttpMethod.GET)
  public Map test2() {
    Map map = new HashMap<>();
    return map;
  }
  @RouteMapping(value = "/test3",method = EnumHttpMethod.GET)
  public Handler<RoutingContext> test3() {
    return event -> {
      HttpServerResponse response = event.response();
      List<Deployment> deployment = vertxManager.getDeployment();
      deployment.forEach(deployment1 -> response.write(Json.encode(deployment1)));
      response.end();
    };
  }
}
