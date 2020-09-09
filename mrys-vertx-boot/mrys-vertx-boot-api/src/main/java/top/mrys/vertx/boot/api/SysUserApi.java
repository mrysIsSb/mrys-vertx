package top.mrys.vertx.boot.api;

import io.vertx.core.Future;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.eventbus.MicroClient;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.http.constants.EnumHttpMethod;

/**
 * @author mrys
 * @date 2020/9/8
 */
@MicroClient
@RouteMapping("/sysUser")
public interface SysUserApi {

  @RouteMapping(value = "/getById",method = EnumHttpMethod.GET)
  Future<SysUser> getById(Integer id);

  default String test() {
    return "yijie";
  }
}
