package top.mrys.vertx.http.starter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

/**
 * @author mrys
 * @date 2020/8/4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HttpStarter.class)
@ComponentScan
public @interface EnableHttp {

  //  configCentre>args>boot>this
  int port() default 8080;

  /**
   * http 配置前缀
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
