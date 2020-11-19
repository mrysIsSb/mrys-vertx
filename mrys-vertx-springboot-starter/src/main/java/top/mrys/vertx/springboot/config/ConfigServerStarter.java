package top.mrys.vertx.springboot.config;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import top.mrys.vertx.common.launcher.MyVerticleFactory;
import top.mrys.vertx.config.starter.ConfigVerticle;

/**
 * @author mrys
 * @date 2020/11/19
 */
@Slf4j
public class ConfigServerStarter implements ApplicationListener<ApplicationStartedEvent>,
    ImportBeanDefinitionRegistrar {

  @Autowired
  private Vertx vertx;

  @Autowired
  private MyVerticleFactory myVerticleFactory;

  private EnableConfigServer enableConfigServer;


  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
      BeanDefinitionRegistry registry) {
    EnableConfigServer enableConfigServer = importingClassMetadata.getAnnotations()
        .get(EnableConfigServer.class)
        .synthesize();
    this.enableConfigServer = enableConfigServer;
    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
        .genericBeanDefinition(ConfigServerStarter.class, () -> this).getBeanDefinition();
    registry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
  }

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    ConfigurableApplicationContext context = event.getApplicationContext();
    vertx.deployVerticle(
        () -> myVerticleFactory
            .getMyAbstractVerticle(top.mrys.vertx.config.starter.ConfigVerticle.class,
                configVerticle -> {
                  configVerticle.setHttpPort(() -> getPort(context));
                  configVerticle.setEnableService(() -> ConfigVerticle.enableHttp);
                  return configVerticle;
                }),
        new DeploymentOptions().setInstances(2), re -> {
          if (re.succeeded()) {
            log.info("配置中心启动");
          } else {
            log.error(re.cause().getMessage(), re.cause());
          }
        });
  }

  protected Integer getPort(ConfigurableApplicationContext context) {
    return context.getEnvironment()
        .getProperty(enableConfigServer.configPrefix() + ".port", Integer.class,
            enableConfigServer.port());
  }

}
