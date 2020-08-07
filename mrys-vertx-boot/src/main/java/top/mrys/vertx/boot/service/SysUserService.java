package top.mrys.vertx.boot.service;

import io.vertx.core.Future;
import top.mrys.vertx.boot.entity.SysUser;

/**
 * @author mrys
 * @date 2020/8/7
 */
public interface SysUserService {

  Future<SysUser> getById(Integer id);
}
