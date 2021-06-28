//package top.mrys.vertx.springboot.config;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import top.mrys.vertx.springboot.http.server.HttpAutoConfiguration;
//import top.mrys.vertx.springboot.http.server.HttpStarter;
//
///**
// * @author mrys
// * @date 2020/11/19
// */
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Import({ConfigServerStarter.class, ConfigServerAutoConfiguration.class,
//    HttpAutoConfiguration.class})
//@Configuration
//public @interface EnableConfigServer {
//
//  //  args>configCentre>boot>this
//  int port() default 9099;
//
//  /**
//   * config.http 配置前缀 config.http.port
//   *
//   * @author mrys
//   */
//  String configPrefix() default "http";
//}
