package top.mrys.vertx.boot.api;

import io.vertx.core.Future;
import java.util.List;
import top.mrys.vertx.boot.entity.Result;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.eventbus.MicroClient;
import top.mrys.vertx.http.annotations.GetRoute;
import top.mrys.vertx.http.annotations.PostRoute;
import top.mrys.vertx.http.annotations.ReqBody;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * @author mrys
 * @date 2020/9/8
 */
@MicroClient()
@RouteMapping("/sysUser")
public interface SysUserApi {

  @RouteMapping(value = "/getById", method = EnumHttpMethod.GET)
  Future<SysUser> getById(Integer id);

/*  @RouteMapping(method = EnumHttpMethod.GET,value = "/robots.txt")
  Future<String> robots();

  @RouteMapping(method = EnumHttpMethod.GET,value = "")
  Future<String> index();*/

  default String test() {
    return "yijie";
  }

  @GetRoute("/getAll")
  Future<Result<List<SysUser>>> getAll();

  @RouteMapping(value = "/test5", method = EnumHttpMethod.GET)
  Future<Integer> test5();

  @PostRoute("/getAll1")
  Future<Result<List<SysUser>>> getAll(@ReqBody SysUser sysUser);

}
