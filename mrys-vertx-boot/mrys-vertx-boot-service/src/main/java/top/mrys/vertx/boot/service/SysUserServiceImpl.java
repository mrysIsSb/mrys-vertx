package top.mrys.vertx.boot.service;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.mrys.vertx.boot.api.SysUserApi;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/9/8
 */
@RouteHandler
@RouteMapping("/sysUser")
public class SysUserServiceImpl implements SysUserApi {

  @Autowired(required = false)
  private MySQLPool mySQLPool;

  @Override
  public Future<SysUser> getById(Integer id) {
    Promise<SysUser> promise = Promise.promise();
    mySQLPool.preparedQuery("select * from sys_user where id=? limit 1")
        .execute(Tuple.wrap(id), event -> {
          if (event.succeeded()) {
            RowSet<Row> result = event.result();
            ArrayList<SysUser> sysUsers = new ArrayList<>();
            result.forEach(row -> {
              SysUser sysUser = new SysUser();
              sysUser.setId(row.getInteger("id"));
              sysUser.setUsername(row.getString("username"));
              sysUsers.add(sysUser);
            });
            if (CollectionUtils.isEmpty(sysUsers)) {
              Throwable throwable = new Throwable("user 为空");
              throwable.setStackTrace(Thread.currentThread().getStackTrace());
              promise.fail(throwable);
            } else {
              promise.complete(sysUsers.get(0));
            }
          } else {
            promise.fail(event.cause());
          }
        });
    return promise.future();
  }
}
