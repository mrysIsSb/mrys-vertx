package top.mrys.vertx.boot.dao.mysql;

import io.vertx.core.Future;
import org.apache.ibatis.annotations.Select;
import top.mrys.vertx.boot.entity.SysUser;

/**
 * @author mrys
 * @date 2020/8/5
 */
public interface SysUserMapper {

  @Select("select * from sys_user limit 1")
  Future<SysUser> getTest();

}
