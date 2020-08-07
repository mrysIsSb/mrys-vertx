package top.mrys.vertx.mybatis.mybatis;

import java.sql.Connection;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.TransactionIsolationLevel;

/**
 * @author mrys
 * @date 2020/8/5
 */
public class MySqlSessionFactory implements SqlSessionFactory {

  private final MyBatisConfiguration configuration;

  public MySqlSessionFactory(MyBatisConfiguration configuration) {
    this.configuration = configuration;
    if (!configuration.getEnumTypes().isEmpty()) {
      configuration.getEnumTypes().forEach(enumType -> {
        Class<? extends EnumType> typeClass = enumType.getClass();
        configuration.getTypeHandlerRegistry().register(typeClass,MyEnumTypeHandler.class);
      });
    }
  }

  @Override
  public SqlSession openSession() {
    return new MySqlSession(configuration);
  }

  @Override
  public SqlSession openSession(boolean autoCommit) {
    return null;
  }

  @Override
  public SqlSession openSession(Connection connection) {
    return null;
  }

  @Override
  public SqlSession openSession(TransactionIsolationLevel level) {
    return null;
  }

  @Override
  public SqlSession openSession(ExecutorType execType) {
    return null;
  }

  @Override
  public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
    return null;
  }

  @Override
  public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
    return null;
  }

  @Override
  public SqlSession openSession(ExecutorType execType, Connection connection) {
    return null;
  }

  @Override
  public Configuration getConfiguration() {
    return configuration;
  }
}
