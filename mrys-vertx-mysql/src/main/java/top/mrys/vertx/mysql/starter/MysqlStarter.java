package top.mrys.vertx.mysql.starter;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.launcher.AbstractStarter;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Configuration
@Slf4j
public class MysqlStarter extends AbstractStarter<EnableMysql> {

  @Autowired
  private Vertx vertx;

  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public void start() {
    ConfigRepo configRepo = ConfigRepo.getInstance();
    String prefix = a.configPrefix();
    JsonObject config = configRepo.getForPath(prefix, JsonObject.class);
    vertx.deployVerticle(new MysqlVerticle(applicationContext.getBean(MysqlSession.class)),
        new DeploymentOptions()
        .setConfig(config),
        event -> {
          if (event.succeeded()) {
            log.info("mysql Verticle succeeded");
          } else {
            log.error("mysql 启动失败", event.cause());
          }
        });
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
