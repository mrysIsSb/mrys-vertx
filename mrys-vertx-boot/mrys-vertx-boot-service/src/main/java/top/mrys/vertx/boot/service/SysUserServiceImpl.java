package top.mrys.vertx.boot.service;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import top.mrys.vertx.boot.api.SysUserApi;
import top.mrys.vertx.boot.entity.Result;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.common.spring.ConditionalOnBean;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.annotations.RouteMapping;
import top.mrys.vertx.mysql.starter.MysqlSession;

/**
 * @author mrys
 * @date 2020/9/8
 */
@RouteHandler
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserApi {

  @Autowired
  private MysqlSession mysqlSession;

  @Override
  public Future<SysUser> getById(Integer id) {
    Promise<SysUser> promise = Promise.promise();
    mysqlSession.preparedQuery("select * from sys_user where id=? limit 1")
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

  @Override
  public Future<SysUser> getById2(Integer id) {
    Scheduler scheduler = Schedulers
        .fromExecutor(command -> Vertx.currentContext().runOnContext(v -> command.run()));
    Flux.just("tom", "jack", "allen")
        .log()
        .publishOn(scheduler)
        .publish(stringFlux -> Flux.just("23423", "345345cxvdf"))
        .filter(s -> s.length() > 3)
        .map(s -> s.concat("@qq.com"))
        .subscribe(System.out::println);
    Promise<SysUser> promise = Promise.promise();
    promise.complete();
    return promise.future();
  }

  @Override
  public Future<Result<List<SysUser>>> getAll() {
    Promise<Result<List<SysUser>>> promise = Promise.promise();
    mysqlSession.query("select * from sys_user")
        .execute(event -> {
          if (event.succeeded()) {
            RowSet<Row> result = event.result();
            ArrayList<SysUser> sysUsers = new ArrayList<>();
            result.forEach(row -> {
              SysUser sysUser = new SysUser();
              sysUser.setId(row.getInteger("id"));
              sysUser.setUsername(row.getString("username"));
              sysUsers.add(sysUser);
            });
            promise.complete(Result.okData(sysUsers));
          } else {
            promise.fail(event.cause());
          }
        });
    return promise.future();
  }

  @Override
  public Future<List<SysUser>> getAll2() {
    Promise<List<SysUser>> promise = Promise.promise();
    mysqlSession.query("select * from sys_user")
        .execute(event -> {
          if (event.succeeded()) {
            RowSet<Row> result = event.result();
            ArrayList<SysUser> sysUsers = new ArrayList<>();
            result.forEach(row -> {
              SysUser sysUser = new SysUser();
              sysUser.setId(row.getInteger("id"));
              sysUser.setUsername(row.getString("username"));
              sysUsers.add(sysUser);
            });
            promise.complete(sysUsers);
          } else {
            promise.fail(event.cause());
          }
        });
    return promise.future();
  }

  @Override
  public Future<Integer> test5() {
    return Future.succeededFuture(1);
  }

  @Override
  public Future<Result<List<SysUser>>> getAll(SysUser user) {
    System.out.println(user);
    Promise<Result<List<SysUser>>> promise = Promise.promise();
    mysqlSession.query("select * from sys_user")
        .execute(event -> {
          if (event.succeeded()) {
            RowSet<Row> result = event.result();
            ArrayList<SysUser> sysUsers = new ArrayList<>();
            result.forEach(row -> {
              SysUser sysUser = new SysUser();
              sysUser.setId(row.getInteger("id"));
              sysUser.setUsername(row.getString("username"));
              sysUsers.add(sysUser);
            });
            promise.complete(Result.okData(sysUsers));
          } else {
            promise.fail(event.cause());
          }
        });
    return promise.future();
  }
}
