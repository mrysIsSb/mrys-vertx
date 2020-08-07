package top.mrys.vertx.mybatis.mybatis;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

/**
 * @author mrys
 * @date 2020/8/5
 */
public class MySqlSession implements SqlSession {

  private final MyBatisConfiguration configuration;

  public MySqlSession(MyBatisConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Retrieve a single row mapped from the statement key.
   *
   * @param statement the statement
   * @return Mapped object
   */
  @Override
  public <T> T selectOne(String statement) {
    return selectOne(statement,null);
  }

  /**
   * Retrieve a single row mapped from the statement key and parameter.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @return Mapped object
   */
  @Override
  public <T> T selectOne(String statement, Object parameter) {
    MappedStatement ms = configuration.getMappedStatement(statement);
    BoundSql boundSql = ms.getBoundSql(parameter);

    return null;
  }

  /**
   * Retrieve a list of mapped objects from the statement key and parameter.
   *
   * @param statement Unique identifier matching the statement to use.
   * @return List of mapped object
   */
  @Override
  public <E> List<E> selectList(String statement) {
    return null;
  }

  /**
   * Retrieve a list of mapped objects from the statement key and parameter.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @return List of mapped object
   */
  @Override
  public <E> List<E> selectList(String statement, Object parameter) {
    return null;
  }

  /**
   * Retrieve a list of mapped objects from the statement key and parameter, within the specified
   * row bounds.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @param rowBounds Bounds to limit object retrieval
   * @return List of mapped object
   */
  @Override
  public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
    return null;
  }

  /**
   * The selectMap is a special case in that it is designed to convert a list of results into a Map
   * based on one of the properties in the resulting objects. Eg. Return a of Map[Integer,Author]
   * for selectMap("selectAuthors","id")
   *
   * @param statement Unique identifier matching the statement to use.
   * @param mapKey    The property to use as key for each value in the list.
   * @return Map containing key pair data.
   */
  @Override
  public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
    return null;
  }

  /**
   * The selectMap is a special case in that it is designed to convert a list of results into a Map
   * based on one of the properties in the resulting objects.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @param mapKey    The property to use as key for each value in the list.
   * @return Map containing key pair data.
   */
  @Override
  public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
    return null;
  }

  /**
   * The selectMap is a special case in that it is designed to convert a list of results into a Map
   * based on one of the properties in the resulting objects.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @param mapKey    The property to use as key for each value in the list.
   * @param rowBounds Bounds to limit object retrieval
   * @return Map containing key pair data.
   */
  @Override
  public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey,
      RowBounds rowBounds) {
    return null;
  }

  /**
   * A Cursor offers the same results as a List, except it fetches data lazily using an Iterator.
   *
   * @param statement Unique identifier matching the statement to use.
   * @return Cursor of mapped objects
   */
  @Override
  public <T> Cursor<T> selectCursor(String statement) {
    return null;
  }

  /**
   * A Cursor offers the same results as a List, except it fetches data lazily using an Iterator.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @return Cursor of mapped objects
   */
  @Override
  public <T> Cursor<T> selectCursor(String statement, Object parameter) {
    return null;
  }

  /**
   * A Cursor offers the same results as a List, except it fetches data lazily using an Iterator.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @param rowBounds Bounds to limit object retrieval
   * @return Cursor of mapped objects
   */
  @Override
  public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
    return null;
  }

  /**
   * Retrieve a single row mapped from the statement key and parameter using a {@code
   * ResultHandler}.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter A parameter object to pass to the statement.
   * @param handler   ResultHandler that will handle each retrieved row
   */
  @Override
  public void select(String statement, Object parameter, ResultHandler handler) {

  }

  /**
   * Retrieve a single row mapped from the statement using a {@code ResultHandler}.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param handler   ResultHandler that will handle each retrieved row
   */
  @Override
  public void select(String statement, ResultHandler handler) {

  }

  /**
   * Retrieve a single row mapped from the statement key and parameter using a {@code ResultHandler}
   * and {@code RowBounds}.
   *
   * @param statement Unique identifier matching the statement to use.
   * @param parameter the parameter
   * @param rowBounds RowBound instance to limit the query results
   * @param handler
   */
  @Override
  public void select(String statement, Object parameter, RowBounds rowBounds,
      ResultHandler handler) {

  }

  /**
   * Execute an insert statement.
   *
   * @param statement Unique identifier matching the statement to execute.
   * @return int The number of rows affected by the insert.
   */
  @Override
  public int insert(String statement) {
    return 0;
  }

  /**
   * Execute an insert statement with the given parameter object. Any generated autoincrement values
   * or selectKey entries will modify the given parameter object properties. Only the number of rows
   * affected will be returned.
   *
   * @param statement Unique identifier matching the statement to execute.
   * @param parameter A parameter object to pass to the statement.
   * @return int The number of rows affected by the insert.
   */
  @Override
  public int insert(String statement, Object parameter) {
    return 0;
  }

  /**
   * Execute an update statement. The number of rows affected will be returned.
   *
   * @param statement Unique identifier matching the statement to execute.
   * @return int The number of rows affected by the update.
   */
  @Override
  public int update(String statement) {
    return 0;
  }

  /**
   * Execute an update statement. The number of rows affected will be returned.
   *
   * @param statement Unique identifier matching the statement to execute.
   * @param parameter A parameter object to pass to the statement.
   * @return int The number of rows affected by the update.
   */
  @Override
  public int update(String statement, Object parameter) {
    return 0;
  }

  /**
   * Execute a delete statement. The number of rows affected will be returned.
   *
   * @param statement Unique identifier matching the statement to execute.
   * @return int The number of rows affected by the delete.
   */
  @Override
  public int delete(String statement) {
    return 0;
  }

  /**
   * Execute a delete statement. The number of rows affected will be returned.
   *
   * @param statement Unique identifier matching the statement to execute.
   * @param parameter A parameter object to pass to the statement.
   * @return int The number of rows affected by the delete.
   */
  @Override
  public int delete(String statement, Object parameter) {
    return 0;
  }

  /**
   * Flushes batch statements and commits database connection. Note that database connection will
   * not be committed if no updates/deletes/inserts were called. To force the commit call {@link
   * SqlSession#commit(boolean)}
   */
  @Override
  public void commit() {

  }

  /**
   * Flushes batch statements and commits database connection.
   *
   * @param force forces connection commit
   */
  @Override
  public void commit(boolean force) {

  }

  /**
   * Discards pending batch statements and rolls database connection back. Note that database
   * connection will not be rolled back if no updates/deletes/inserts were called. To force the
   * rollback call {@link SqlSession#rollback(boolean)}
   */
  @Override
  public void rollback() {

  }

  /**
   * Discards pending batch statements and rolls database connection back. Note that database
   * connection will not be rolled back if no updates/deletes/inserts were called.
   *
   * @param force forces connection rollback
   */
  @Override
  public void rollback(boolean force) {

  }

  /**
   * Flushes batch statements.
   *
   * @return BatchResult list of updated records
   * @since 3.0.6
   */
  @Override
  public List<BatchResult> flushStatements() {
    return null;
  }

  /**
   * Closes the session.
   */
  @Override
  public void close() {

  }

  /**
   * Clears local session cache.
   */
  @Override
  public void clearCache() {

  }

  /**
   * Retrieves current configuration.
   *
   * @return Configuration
   */
  @Override
  public Configuration getConfiguration() {
    return configuration;
  }

  /**
   * Retrieves a mapper.
   *
   * @param type Mapper interface class
   * @return a mapper bound to this SqlSession
   */
  @Override
  public <T> T getMapper(Class<T> type) {
    return configuration.getMapperRegistry().getMapper(type,this);
  }

  /**
   * Retrieves inner database connection.
   *
   * @return Connection
   */
  @Override
  public Connection getConnection() {
    return null;
  }
}
