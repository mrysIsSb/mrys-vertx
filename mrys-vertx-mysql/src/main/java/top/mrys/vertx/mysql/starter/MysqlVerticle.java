package top.mrys.vertx.mysql.starter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Slf4j
public class MysqlVerticle extends MyAbstractVerticle {

  @Autowired
  private MyRefreshableApplicationContext applicationContext;


  @Override
  public void start() throws Exception {
    log.info("mysql 服务启动中。。。");
   /* MySQLPool pool = mySQLPool(vertx);
    applicationContext.registerBean(MySQLPool.class,() -> pool);*/
  }

/*  private MySQLPool mySQLPool(Vertx vertx) {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
        .setCachePreparedStatements(true)
        .setPort(3306)
        .setHost("192.168.124.16")
        .setDatabase("test")
        .setUser("root")
        .setPassword("123456");
    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);
    return MySQLPool.pool(vertx,connectOptions, poolOptions);
  }*/


  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    applicationContext.doBeanAndRemoveBean(o -> o.close(stopPromise),MySQLPool.class);
  }

}
