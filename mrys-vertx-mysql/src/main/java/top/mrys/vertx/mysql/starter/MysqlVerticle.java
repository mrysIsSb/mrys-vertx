package top.mrys.vertx.mysql.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.impl.VertxImpl;
import io.vertx.mysqlclient.MySQLPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Component
public class MysqlVerticle extends AbstractVerticle {

  @Autowired
  private MyRefreshableApplicationContext applicationContext;

  @Autowired
  private MySQLPool pool;


  @Override
  public void start() throws Exception {
    System.out.println("mysql启动");
  }

  @Override
  public void stop() throws Exception {
  /*  VertxImpl vertx1 = (VertxImpl) vertx;
    vertx1.addCloseHook(completion -> {
      System.out.println("关闭mysql");*/
      applicationContext.removeBean(MySQLPool.class);
      pool.close();
//      pool.close(completion);
    /*});*/
  }
}
