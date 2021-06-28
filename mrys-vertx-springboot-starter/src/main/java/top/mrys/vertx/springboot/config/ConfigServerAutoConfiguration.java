//package top.mrys.vertx.springboot.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Scope;
//import top.mrys.vertx.common.config.ConfigLoader;
//import top.mrys.vertx.common.launcher.ApplicationContext;
//import top.mrys.vertx.config.controller.ConfigController;
//import top.mrys.vertx.config.starter.ConfigVerticle;
//
///**
// * @author mrys
// * @date 2020/11/20
// */
//public class ConfigServerAutoConfiguration {
//
//  @Autowired
//  private ApplicationContext context = new ApplicationContext();
//
//  @Bean
//  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//  public ConfigVerticle configVerticle() {
//    ConfigVerticle verticle = new ConfigVerticle();
//    if (verticle.getContext() == null) {
//      verticle.setContext(context);
//    }
//    return verticle;
//  }
//
//  @Bean
//  public ConfigController configController(ConfigLoader configLoader) {
//    ConfigController configController = new ConfigController();
//    configController.setConfigLoader(configLoader);
//    return configController;
//  }
//}
