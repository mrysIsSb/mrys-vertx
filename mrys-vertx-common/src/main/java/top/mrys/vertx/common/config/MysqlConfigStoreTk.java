package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import java.util.List;
import java.util.function.BiFunction;
import lombok.extern.slf4j.Slf4j;

/**
 * 表名是keys 环境是字段名
 *
 * @author mrys
 * @date 2020/11/22
 */
@Slf4j
public class MysqlConfigStoreTk implements MyConfigStoreTk, ConfigStore {

  private MySQLPool client;

  private List<String> keys;

  /**
   * 获取store的type 用在配置文件的 storeType
   *
   * @author mrys
   */
  @Override
  public String getStoreName() {
    return "mysql";
  }

  /**
   * 检查是否可用;
   *
   * @param configuration
   * @author mrys
   */
  @Override
  public boolean check(JsonObject configuration) {
    log.info("{}", configuration);
    try {
      getClass().getClassLoader().loadClass("io.vertx.mysqlclient.MySQLPool");
    } catch (ClassNotFoundException e) {
      log.warn("io.vertx.mysqlclient.MySQLPool 不存在");
      return false;
    }
    return true;
  }

  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    JsonObject config = getDealPostConfig(configuration);
    keys = config.getJsonArray("keys").getList();
    //连接配置
    MySQLConnectOptions connectOptions = new MySQLConnectOptions(
        config.getJsonObject("connectOptions"));
    //连接池配置
    PoolOptions poolOptions = new PoolOptions(config.getJsonObject("poolOptions"));
    client = MySQLPool.pool(vertx, connectOptions, poolOptions);
    return this;
  }

  /**
   * key 和 profile 拼接方法
   *
   * @author mrys
   */
  @Override
  public BiFunction<String, String, String> getJointKeyAndProfile() {
    return (s, s2) -> s + "_" + s2;
  }


  @Override
  public void get(Handler<AsyncResult<Buffer>> completionHandler) {
    client.getConnection().onComplete(event -> {
      if (event.succeeded()) {
        SqlConnection connection = event.result();
        String table = "config";//todo
        connection.preparedQuery("select * from " + table + " ").execute(ar -> {
          if (ar.succeeded()) {
            RowSet<Row> result = ar.result();
            System.out.println("Got " + result.size() + " rows ");
          } else {
            System.out.println("Failure: " + ar.cause().getMessage());
          }
          connection.close();
        });
      }else {
        completionHandler.handle(Future.failedFuture(event.cause()));
      }
    });
  }

  @Override
  public void close(Handler<Void> completionHandler) {
    log.info("client close");
    client.close();
  }
}
