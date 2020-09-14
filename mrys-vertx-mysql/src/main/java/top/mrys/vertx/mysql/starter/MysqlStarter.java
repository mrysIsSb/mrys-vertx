package top.mrys.vertx.mysql.starter;

import io.vertx.core.impl.VertxImpl;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.launcher.AbstractStarter;

/**
 * @author mrys
 * @date 2020/8/5
 */
public class MysqlStarter extends AbstractStarter<EnableMysql> {

  @Autowired
  private VertxImpl vertx;

  @Override
  public void start(EnableMysql enableMysql) {
    vertx.deployVerticle(new MysqlVerticle());
  }

  @Override
  protected void postRegisterBeanDefinitions(EnableMysql annotation,
      BeanDefinitionRegistry registry) {
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
    scanner.scan(getClass().getPackage().getName());
  /*  BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
        .genericBeanDefinition(MysqlVerticle.class);
    registry.registerBeanDefinition(MysqlVerticle.class.getSimpleName(), beanDefinitionBuilder.getBeanDefinition());*/
    super.postRegisterBeanDefinitions(annotation, registry);
  }
}
