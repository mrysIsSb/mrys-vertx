package top.mrys.vertx.http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mrys
 * 2021年7月6日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Path {
  /**
   * 地址
   *
   * @author mrys
   */
  String value();

  /**
   * 请求方法
   *
   * @author mrys
   */
  String method() default "GET";
}
