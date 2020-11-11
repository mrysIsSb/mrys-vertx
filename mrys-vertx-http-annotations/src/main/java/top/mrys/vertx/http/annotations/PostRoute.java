package top.mrys.vertx.http.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import top.mrys.vertx.http.constants.EnumHttpMethod;


/**
 * @author mrys
 * @date 2020/9/26
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@RouteMapping(method = EnumHttpMethod.POST)
public @interface PostRoute {

  @AliasFor(annotation = RouteMapping.class)
  String[] value() default {};
}
