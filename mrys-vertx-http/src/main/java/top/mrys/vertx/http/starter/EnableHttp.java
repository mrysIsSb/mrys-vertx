package top.mrys.vertx.http.starter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author mrys
 * @date 2020/8/4
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HttpStarter.class)
public @interface EnableHttp {

  int value() default 0;
  int port() default 8080;
  String[] scanPackage();
}
