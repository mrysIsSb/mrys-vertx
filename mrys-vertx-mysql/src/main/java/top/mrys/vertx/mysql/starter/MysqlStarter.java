package top.mrys.vertx.mysql.starter;

import io.vertx.core.impl.VertxImpl;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.common.launcher.AbstractStarter;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Configuration
public class MysqlStarter extends AbstractStarter<EnableMysql> {


  @Autowired
  private MyRefreshableApplicationContext context;

  @Autowired
  private VertxImpl vertx;

  @Override
  public void start(EnableMysql enableMysql) {
    vertx.deployVerticle(mysqlVerticle());
  }

  @Bean
//  @Conditional()
  public MySQLPool mySQLPool() {
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
    vertx.addCloseHook(completion -> {
      System.out.println("关闭mysql");
      pool.close(completion);
    });
    return pool;
  }

  @Bean
  public MysqlVerticle mysqlVerticle() {
    return new MysqlVerticle();
  }

}
