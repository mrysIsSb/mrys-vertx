package top.mrys.vertx.mysql.starter;

import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxImpl;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Configuration
public class MysqlVerticle extends MyAbstractVerticle {

  @Autowired
  private MyRefreshableApplicationContext applicationContext;

/*  @Autowired
  private MySQLPool pool;*/


  @Override
  public void start() throws Exception {
    System.out.println("mysql启动");
  }

  @Bean
  public MySQLPool mySQLPool(Vertx vertx) {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
        .setCachePreparedStatements(true)
        .setPort(3306)
        .setHost("192.168.124.16")
        .setDatabase("test")
        .setUser("root")
        .setPassword("123456");
    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);
    MySQLPool pool = MySQLPool.pool(vertx,connectOptions, poolOptions);
    /*((VertxImpl)vertx).addCloseHook(completion -> {
      System.out.println("关闭mysql");
      pool.close(completion);
    });*/
    return pool;
  }


  @Override
  public void stop() throws Exception {
    applicationContext.doBeanAndRemoveBean(MySQLPool::close,MySQLPool.class);
  }
}
