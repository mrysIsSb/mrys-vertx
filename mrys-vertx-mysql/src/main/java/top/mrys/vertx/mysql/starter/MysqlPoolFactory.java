package top.mrys.vertx.mysql.starter;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PreparedQuery;
import io.vertx.sqlclient.Query;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author mrys
 * @date 2020/9/12
 */
@Component
public class MysqlPoolFactory implements FactoryBean<MySQLPool> {

  @Setter
  private MySQLPool mySQLPool=new MySQLPool() {
    @Override
    public void getConnection(Handler<AsyncResult<SqlConnection>> handler) {
    }

    @Override
    public Future<SqlConnection> getConnection() {
      return null;
    }

    @Override
    public Query<RowSet<Row>> query(String sql) {
      return null;
    }

    @Override
    public PreparedQuery<RowSet<Row>> preparedQuery(String sql) {
      return null;
    }

    @Override
    public void close(Handler<AsyncResult<Void>> handler) {
    }

    @Override
    public Future<Void> close() {
      return null;
    }
  };

  @Override
  public MySQLPool getObject() throws Exception {
    return mySQLPool;
  }


  @Override
  public Class<?> getObjectType() {
    return MySQLPool.class;
  }
}
