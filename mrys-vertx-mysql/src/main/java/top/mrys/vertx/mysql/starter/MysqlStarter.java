package top.mrys.vertx.mysql.starter;

import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import top.mrys.vertx.common.launcher.AbstractStarter;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;
import top.mrys.vertx.common.utils.ScanPackageUtil;
import top.mrys.vertx.mysql.annotations.MapperScan;
import top.mrys.vertx.mysql.mybatis.MyBatisConfiguration;
import top.mrys.vertx.mysql.mybatis.MyExecutor;
import top.mrys.vertx.mysql.mybatis.MySqlSessionFactory;

/**
 * @author mrys
 * @date 2020/8/5
 */
//@ComponentScan(basePackages = "top.mrys.vertx.mysql.mybatis")
@Configuration
public class MysqlStarter extends AbstractStarter<EnableMysql> {


  @Autowired
  private MyRefreshableApplicationContext context;


  @Override
  public void start(EnableMysql enableMysql) {
//    ClassPathBeanDefinitionScanner
    MyBatisConfiguration configuration = new MyBatisConfiguration();
    MySqlSessionFactory mySqlSessionFactory = new MySqlSessionFactory(configuration);
    try {
      Map<String, Object> beansWithAnnotation = context
          .getBeansWithAnnotation(MapperScan.class);
      beansWithAnnotation.values().forEach(o -> {
        Set<MapperScan> mapperScans = AnnotatedElementUtils
            .findAllMergedAnnotations(o.getClass(), MapperScan.class);
        mapperScans.forEach(mapperScan -> {
          String value = mapperScan.value();
          Class[] classes = ScanPackageUtil.getClassFromPackage(value);
          for (Class aClass : classes) {
            if (aClass.isInterface()) {
              configuration.addMapper(aClass);
              context
                  .registerBean(aClass, () -> mySqlSessionFactory.openSession().getMapper(aClass));
            }
          }
        });
      });
    } catch (BeansException e) {

    }

  }

  @Bean
  public MySQLPool mySQLPool() {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
        .setPort(3306)
        .setHost("localhost")
        .setUser("root")
        .setPassword("123456");

    PoolOptions poolOptions = new PoolOptions()
        .setMaxSize(5);
    return MySQLPool.pool(connectOptions, poolOptions);
  }


  @Bean
  public MyBatisConfiguration myBatisConfiguration() {
    return new MyBatisConfiguration();
  }

  @Bean
  public MySqlSessionFactory mySqlSessionFactory(MyBatisConfiguration configuration) {
    return new MySqlSessionFactory(configuration);
  }

  @Bean
  public MyExecutor myExecutor(MyBatisConfiguration configuration, MySQLPool pool) {
    return new MyExecutor(configuration,null, pool);
  }
}
