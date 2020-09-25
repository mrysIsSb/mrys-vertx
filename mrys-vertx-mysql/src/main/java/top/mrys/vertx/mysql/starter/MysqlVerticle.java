package top.mrys.vertx.mysql.starter;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Slf4j
public class MysqlVerticle extends MyAbstractVerticle {

  private MysqlSession mysqlSession;


  public MysqlVerticle(MysqlSession mysqlSession) {
    this.mysqlSession = mysqlSession;
  }

  @Override
  public void start() throws Exception {
    log.info("mysql 服务启动中。。。");
    JsonObject config = vertx.getOrCreateContext().config();
    mysqlSession.init(connectOptions(config.getJsonObject("connectOptions")), poolOptions(config.getJsonObject("poolOptions")));
  }

  private MySQLConnectOptions connectOptions(JsonObject config) {
    return new MySQLConnectOptions(config);
  }

  private PoolOptions poolOptions(JsonObject config) {
    return new PoolOptions(config);
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    mysqlSession.close(stopPromise);
  }

}
