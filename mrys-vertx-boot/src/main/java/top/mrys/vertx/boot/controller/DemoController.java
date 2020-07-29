package top.mrys.vertx.boot.controller;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;
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

  @RouteMapping(value = "test1",method = EnumHttpMethod.GET)
  public Handler<RoutingContext> test1() {
    return event -> {
      event.response().end("hello world");
    };
  }

  @RouteHeader(name="headerName",value = "value123123")
  @RouteHeader(name="headerName",value = "value123123")
  @RouteMapping(value = "test2",method = EnumHttpMethod.GET)
  public Map test2() {
    Map map = new HashMap<>();
    return map;
  }

}
