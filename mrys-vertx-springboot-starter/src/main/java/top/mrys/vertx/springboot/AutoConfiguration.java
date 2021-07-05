package top.mrys.vertx.springboot;

import io.vertx.core.Launcher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Configuration
@Import({MainVerticle.class,CustomVerticleFactory.class})
//@EnableConfigurationProperties(Red.class)
public class AutoConfiguration {

  @Bean
  public Launcher launcher(ApplicationArguments args, CustomVerticleFactory factory) {
    SpringLauncher launcher = new SpringLauncher(factory);
    launcher.dispatch(args.getSourceArgs());//部署mainverticle
    return launcher;
  }

//
//  @Bean
//  public HttpClient httpClient(Vertx vertx) {
//    return vertx.createHttpClient();
//  }
//
//  /**
//   * 对象实例化工厂
//   *
//   * @author mrys
//   */
//  @Bean
//  public ObjectInstanceFactory objectInstanceFactory() {
//    return VertxRelevantObjectInstanceFactory.objectInstanceFactory();
//  }
//
//  @Bean
//  public ApplicationContext context() {
//    return VertxRelevantObjectInstanceFactory.context();
//  }
//
//  @Bean
//  public ConfigLoader configLoader() {
//    return VertxRelevantObjectInstanceFactory.getConfigLoader();
//  }
//
//  /**
//   * verticle实例化工厂
//   *
//   * @author mrys
//   */
//  @Bean
//  public MyVerticleFactory myVerticleFactory() {
//    MyVerticleFactory verticleFactory = new MyVerticleFactory();
//    verticleFactory.setInstanceFactory(objectInstanceFactory());
//    verticleFactory.setContext(context());
//    return verticleFactory;
//  }
//
//  @Bean
//  public ConfigRepo vertxConfig() {
//    return VertxRelevantObjectInstanceFactory.getConfigLoader().load();
//  }
//
//  @ConfigurationProperties(prefix = "another")
//  //todo 刷新
//  @Bean
//  public Red anotherComponent() {
//    return new Red();
//  }
//
//  @ConfigurationProperties("acme")
//  public static class Red extends ConfigCentreStoreOptions {
//
//  }

}
