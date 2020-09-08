package top.mrys.vertx.boot.api;

import io.vertx.core.Future;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.eventbus.MicroClient;
import top.mrys.vertx.http.annotations.RouteHeader;

/**
 * @author mrys
 * @date 2020/9/8
 */
@MicroClient()
public interface SysUserApi {

  Future<SysUser> getById(@RouteHeader Integer id);
}
