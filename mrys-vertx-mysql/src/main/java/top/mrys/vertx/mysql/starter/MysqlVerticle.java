package top.mrys.vertx.mysql.starter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Slf4j
@Deprecated
public class MysqlVerticle extends MyAbstractVerticle {

  @Autowired
  private MyRefreshableApplicationContext applicationContext;


  @Override
  public void start() throws Exception {
    log.info("mysql 服务启动中。。。");
   /* MySQLPool pool = mySQLPool(vertx);
    applicationContext.registerBean(MySQLPool.class,() -> pool);*/
  }


  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    applicationContext.doBeanAndRemoveBean(o -> o.close(stopPromise),MySQLPool.class);
  }

}
