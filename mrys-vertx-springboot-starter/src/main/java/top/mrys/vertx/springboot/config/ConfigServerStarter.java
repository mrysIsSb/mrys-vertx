package top.mrys.vertx.springboot.config;

import cn.hutool.core.util.ObjectUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
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
import top.mrys.vertx.common.config.ConfigLoader;
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


  private EnableConfigServer enableConfigServer;

  @Autowired
  private ConfigLoader configLoader;


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
    Object hc = context.getEnvironment()
        .getProperty(enableConfigServer.configPrefix(), Object.class);
    HttpServerOptions serverOptions = new HttpServerOptions(
        JsonObject.mapFrom(ObjectUtil.defaultIfNull(hc, new HashMap<>())));
    vertx.deployVerticle(() -> {
          ConfigVerticle bean = context.getBean(ConfigVerticle.class);
          bean.setServerOptions(serverOptions);
          bean.setEnableService(ConfigVerticle.enableHttp);
          return bean;
        }, new DeploymentOptions().setConfig(configLoader.getConfig()).setInstances(1),
        re -> {
          if (re.succeeded()) {
            log.info("配置中心启动:{}", serverOptions.getPort());
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
