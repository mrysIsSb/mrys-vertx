package top.mrys.vertx.mysql.starter;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.PreparedQuery;
import io.vertx.sqlclient.Query;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import javax.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author mrys
 * @date 2020/9/15
 */
public class MysqlSession implements MySQLPool {

  @Setter
  private MySQLPool mySQLPool;
  @Autowired
  private Vertx vertx;

  @PostConstruct
  public void init() {
    mySQLPool = mySQLPool(vertx);
  }
  private MySQLPool mySQLPool(Vertx vertx) {
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
  }

  public MySQLPool getMySQLPool() {
    if (mySQLPool == null) {
      throw new NullPointerException("mysql 未连接");
    }
    return mySQLPool;
  }

  @Override
  public void getConnection(Handler<AsyncResult<SqlConnection>> handler) {
    getMySQLPool().getConnection(handler);
  }


  @Override
  public Future<SqlConnection> getConnection() {
    return getMySQLPool().getConnection();
  }

  @Override
  public Query<RowSet<Row>> query(String sql) {
    return getMySQLPool().query(sql);
  }


  @Override
  public PreparedQuery<RowSet<Row>> preparedQuery(String sql) {
    return getMySQLPool().preparedQuery(sql);
  }


  @Override
  public void close(Handler<AsyncResult<Void>> handler) {
    getMySQLPool().close(handler);
  }


  @Override
  public Future<Void> close() {
    return getMySQLPool().close();
  }
}
