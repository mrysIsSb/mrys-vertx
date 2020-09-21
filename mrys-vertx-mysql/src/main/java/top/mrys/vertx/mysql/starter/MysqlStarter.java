package top.mrys.vertx.mysql.starter;

import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.PoolOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.launcher.AbstractStarter;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Configuration
public class MysqlStarter extends AbstractStarter<EnableMysql> {

  @Bean
  public MySQLConnectOptions connectOptions(ConfigRepo repo) {
    return new MySQLConnectOptions()
        .setCachePreparedStatements(true)
        .setPort(3306)
        .setHost("192.168.124.16")
        .setDatabase("test")
        .setUser("root")
        .setPassword("123456");
  }

  @Bean
  public PoolOptions poolOptions(ConfigRepo repo) {
    return new PoolOptions()
        .setMaxSize(5);
  }

  @Override
  public void start() {
    System.out.println("11111111111111");
    //todo 改为回调方式
//    vertx.deployVerticle(new MysqlVerticle());
  }

  @Override
  protected void postRegisterBeanDefinitions(EnableMysql annotation,
      BeanDefinitionRegistry registry) {
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
    scanner.scan(getClass().getPackage().getName());
    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
        .genericBeanDefinition(MysqlSession.class);
    registry.registerBeanDefinition(MysqlSession.class.getSimpleName(),
        beanDefinitionBuilder.getBeanDefinition());
  }
}
