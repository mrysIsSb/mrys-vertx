package top.mrys.vertx.springboot.http.server.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import top.mrys.vertx.http.annotations.RouteHandler;
import top.mrys.vertx.springboot.http.server.HttpAutoConfiguration;
import top.mrys.vertx.springboot.http.server.HttpStarter;

/**
 * 扫描会与springapplication重复
 *
 * @author mrys
 * @date 2020/8/4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(includeFilters = {
    @Filter(value = RouteHandler.class, type = FilterType.ANNOTATION)}, useDefaultFilters = false)
@Import({HttpStarter.class, HttpAutoConfiguration.class})
@Configuration
public @interface EnableHttp {

  //  args>configCentre>boot>this
  int port() default 8080;

  /**
   * http 配置前缀 http.port
   *
   * @author mrys
   */
  String configPrefix() default "http";

  /**
   * 扫描routehandler类
   *
   * @author mrys
   */
  @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
  String[] scanPackage();
}
