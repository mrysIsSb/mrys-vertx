package top.mrys.vertx.springboot.micro;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author mrys
 * @date 2020/11/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(MicroAutoConfiguration.class)
public @interface EnableMicroClient {

  /**
   * 要扫描带有MicroClient的接口
   *
   * @author mrys
   */
  String[] value() default {};
}
