package top.mrys.vertx.springboot.http.server;

import cn.hutool.core.util.ObjectUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
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
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.http.starter.HttpVerticle;
import top.mrys.vertx.springboot.http.server.annotations.EnableHttp;

/**
 * @author mrys
 * @date 2020/8/4
 */
@Slf4j
public class HttpStarter implements ApplicationListener<ApplicationStartedEvent>,
    ImportBeanDefinitionRegistrar {


  @Autowired
  private Vertx vertx;

  private EnableHttp enableHttp;

  @Autowired
  private ConfigLoader configLoader;


  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
      BeanDefinitionRegistry registry) {
    EnableHttp enableHttp = importingClassMetadata.getAnnotations().get(EnableHttp.class)
        .synthesize();
    this.enableHttp = enableHttp;
    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
        .genericBeanDefinition(HttpStarter.class, () -> this).getBeanDefinition();
    registry.registerBeanDefinition(beanDefinition.getBeanClassName(), beanDefinition);
  }

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    ConfigurableApplicationContext context = event.getApplicationContext();
    Object hc = context.getEnvironment()
        .getProperty(enableHttp.configPrefix(), Object.class);
    HttpServerOptions serverOptions = new HttpServerOptions(
        JsonObject.mapFrom(ObjectUtil.defaultIfNull(hc, new HashMap<>())));
    vertx.deployVerticle(() -> {
          HttpVerticle bean = context.getBean(HttpVerticle.class);
          bean.setServerOptions(serverOptions);
          bean.setRouteClass(getRouteClass(context));
          return bean;
        }, new DeploymentOptions().setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE)/*todo 自定义*/,
        re -> {
          if (re.succeeded()) {
            log.info("http server started port:{}", serverOptions.getPort());
          } else {
            log.error(re.cause().getMessage(), re.cause());
          }
        });
  }
/*vertx.deployVerticle(
        () -> myVerticleFactory.getMyAbstractVerticle(HttpVerticle.class, httpVerticle -> {
          httpVerticle.setPort(() -> getPort(context));
          httpVerticle.setRouteClassProvider(() -> getRouteClass(context));
          return httpVerticle;
        }),
        new DeploymentOptions().setConfig(configLoader.getConfig()).setInstances(VertxOptions.DEFAULT_EVENT_LOOP_POOL_SIZE), re -> {
          if (re.succeeded()) {
            log.info("http server started port:{}", getPort(context));
          } else {
            log.error(re.cause().getMessage(), re.cause());
          }
        });*/


  protected Integer getPort(ConfigurableApplicationContext context) {
    return context.getEnvironment()
        .getProperty(enableHttp.configPrefix() + ".port", Integer.class, enableHttp.port());
  }

  /**
   * 获取扫描包下的class
   *
   * @author mrys
   */
  @SneakyThrows
  protected Set<Class> getRouteClass(ConfigurableApplicationContext context) {
    /*String[] packages = enableHttp.scanPackage();
    Set<Class> clazzes = context.getBeansWithAnnotation(RouteHandler.class).values()
        .stream()
        .filter(o -> {
          String name = o.getClass().getName();
          boolean isIn = false;
          for (int i = 0; i < packages.length && !isIn; i++) {
            if (name.indexOf(packages[i]) != -1) {
              isIn = true;
            }
          }
          return isIn;
        }).map(Object::getClass).collect(Collectors.toSet());*/
    return context.getBeansWithAnnotation(RouteHandler.class).values().stream()
        .map(Object::getClass).collect(
            Collectors.toSet());
  }
/*

  protected void registerRoute(BeanDefinitionRegistry registry) {
    ClassPathScanningCandidateComponentProvider scanner = getScanner(environment);
    scanner.setResourceLoader(resourceLoader);
    AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(RouteHandler.class);
    Set<String> basePackages = getBasePackages();

    scanner.addIncludeFilter(annotationTypeFilter);

    for (String basePackage : basePackages) {
      Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
      for (BeanDefinition candidateComponent : candidateComponents) {
        if (candidateComponent instanceof AnnotatedBeanDefinition) {
          AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
          AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
          if (!annotationMetadata.isConcrete()) {
            log.warn("@RouteHandler 只能用在具体类上:{}", annotationMetadata.getClassName());
          } else {
            //注册到容器
            register(registry, annotationMetadata);
          }
        }
      }
    }
  }

  private void register(BeanDefinitionRegistry registry,
      AnnotationMetadata annotationMetadata) {
    String className = annotationMetadata.getClassName();
    BeanDefinitionBuilder definition = BeanDefinitionBuilder
        .genericBeanDefinition(className);
    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

    AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
    beanDefinition.setPrimary(true);
    registry.registerBeanDefinition(className, beanDefinition);
  }


  private Set<String> getBasePackages() {
    HashSet<String> packages = new HashSet<>();
    packages.addAll(Arrays.asList(enableHttp.scanPackage()));
    return packages;
  }

  */
/**
 * 获取扫描工具
 *
 * @author mrys
 *//*

  protected ClassPathScanningCandidateComponentProvider getScanner(Environment environment) {
    return new ClassPathScanningCandidateComponentProvider(false, environment) {
      @Override
      protected boolean isCandidateComponent(
          AnnotatedBeanDefinition beanDefinition) {
        boolean isCandidate = false;
        if (beanDefinition.getMetadata().isIndependent()) {
          if (!beanDefinition.getMetadata().isAnnotation()) {
            isCandidate = true;
          }
        }
        return isCandidate;
      }
    };
  }
*/

}

