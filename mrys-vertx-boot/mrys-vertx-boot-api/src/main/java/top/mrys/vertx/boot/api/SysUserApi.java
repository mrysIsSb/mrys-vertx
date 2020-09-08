package top.mrys.vertx.boot.api;

import io.vertx.core.Future;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.eventbus.MicroClient;

/**
 * @author mrys
 * @date 2020/9/8
 */
@MicroClient()
public interface SysUserApi {

  Future<SysUser> getById(Integer id);
}
