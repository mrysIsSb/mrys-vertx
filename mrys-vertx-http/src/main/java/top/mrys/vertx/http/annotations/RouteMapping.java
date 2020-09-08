package top.mrys.vertx.http.annotations;


import java.lang.annotation.Inherited;
import top.mrys.vertx.http.constants.EnumHttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mrys
 * @date 2020/7/4
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RouteMapping {

    String value();
    EnumHttpMethod method() default EnumHttpMethod.NONE;
}
