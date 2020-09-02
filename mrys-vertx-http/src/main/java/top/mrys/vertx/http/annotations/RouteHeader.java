package top.mrys.vertx.http.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mrys
 * @date 2020/7/9
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RouteHeaders.class)
public @interface RouteHeader {

    /**
     * header值
     *
     * @author mrys
     */
    String value();

    /**
     * header名称
     *
     * @author mrys
     */
    String name();
}

