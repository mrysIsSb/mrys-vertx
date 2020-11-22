package top.mrys.vertx.boot.routes;

import cn.hutool.core.convert.Convert;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.lang.reflect.Method;
import java.util.List;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import top.mrys.vertx.boot.api.SysUserApi;
import top.mrys.vertx.boot.entity.Result;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.common.utils.ASMUtil;
import top.mrys.vertx.common.utils.Test;
import top.mrys.vertx.springboot.http.server.annotations.PostRoute;
import top.mrys.vertx.http.annotations.ReqBody;
import top.mrys.vertx.http.annotations.RouteHandler;
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

  /*@Autowired
  private VertxManager vertxManager;*/

  private int i;

  @RouteMapping(value = "/test1", method = EnumHttpMethod.GET)
  public Handler<RoutingContext> test1() {
    return event -> {
      String id = event.request().getParam("id");
      System.out.println(sysUserApi.test());
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
  @RouteMapping(value = "/test3", method = EnumHttpMethod.GET)
  public Future<Integer> test3() {
    Promise<Integer> promise = Promise.promise();
    promise.complete(++i);
    return promise.future();
  }

  @RouteMapping(value = "/test4", method = EnumHttpMethod.GET)
  public Future<Result<List<SysUser>>> test4() {
    return sysUserApi.getAll();
  }

  @RouteMapping(value = "/test5", method = EnumHttpMethod.GET)
  public Future<Integer> test5() {
    return sysUserApi.test5();
  }

  @SneakyThrows
  @RouteMapping(value = "/test6", method = EnumHttpMethod.GET)
  public Future<SysUser> test6(Integer id) {
    Method[] methods = Test.class.getMethods();
    Method method = methods[0];
    System.out.println(ASMUtil.getMethodParamNames(method));
    DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    String[] parameterNames = discoverer.getParameterNames(method);
    System.out.println(parameterNames);
    return sysUserApi.getById(id);
  }

  @PostRoute
  public Future<Result<List<SysUser>>> test7(@ReqBody SysUser sysUser) {
    return sysUserApi.getAll(sysUser);
  }

  @PostRoute
  public Future<List<SysUser>> test8() {
    return sysUserApi.getAll2();
  }

}
