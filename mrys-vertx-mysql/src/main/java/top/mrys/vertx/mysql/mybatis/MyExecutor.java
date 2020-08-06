package top.mrys.vertx.mysql.mybatis;

import cn.hutool.core.collection.CollectionUtil;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.EventLoopContext;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import java.sql.SQLException;
import java.util.List;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

/**
 * @author mrys
 * @date 2020/8/6
 */
public class MyExecutor extends BaseExecutor {

  private final MySQLPool pool;

  public MyExecutor(Configuration configuration,
      Transaction transaction, MySQLPool pool) {
    super(configuration, transaction);
    this.pool = pool;
  }

  @Override
  protected int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
    return 0;
  }

  @Override
  protected List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException {
    return null;
  }

  @Override
  protected <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds,
      ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
    String sql = boundSql.getSql();
    Future<RowSet<Row>> execute = pool.query(sql).execute();
    for (;execute.isComplete(); ) {
      RowSet<Row> result = execute.result();
      return CollectionUtil.isNotEmpty(result)?null:null;
    }
    return null;
  }

  @Override
  protected <E> Cursor<E> doQueryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds,
      BoundSql boundSql) throws SQLException {
    return null;
  }
}
