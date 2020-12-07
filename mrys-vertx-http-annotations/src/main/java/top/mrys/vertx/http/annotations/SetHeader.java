package top.mrys.vertx.http.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import top.mrys.vertx.http.interfaces.HeaderValueProcess;

/**
 * 设置http请求头
 *
 * @author mrys
 * @date 2020/12/7
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SetHeader {


  Header[] headers() default {};

  @Retention(RetentionPolicy.RUNTIME)
  @Target({})
  @interface Header {

    String key();

    Class<? extends HeaderValueProcess> processClass();

    String[] args() default {};
  }
}
