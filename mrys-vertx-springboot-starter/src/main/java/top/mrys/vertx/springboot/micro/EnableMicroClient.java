package top.mrys.vertx.springboot.micro;

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
import top.mrys.vertx.eventbus.MicroClient;

/**
 * @author mrys
 * @date 2020/11/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ComponentScan(includeFilters = {
    @Filter(value = MicroClient.class, type = FilterType.ANNOTATION)}, useDefaultFilters = false)
@Import({MicroAutoConfiguration.class})
@Configuration
public @interface EnableMicroClient {

  /**
   * 要扫描带有MicroClient的接口
   *
   * @author mrys
   */
  @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
  String[] value() default {};
}
