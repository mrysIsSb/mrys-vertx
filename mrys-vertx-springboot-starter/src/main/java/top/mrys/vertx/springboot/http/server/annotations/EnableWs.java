package top.mrys.vertx.springboot.http.server.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import top.mrys.vertx.springboot.http.server.WsAutoConfiguration;
import top.mrys.vertx.springboot.http.server.WsStarter;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({WsStarter.class, WsAutoConfiguration.class})
public @interface EnableWs {

  int port() default 8081;

}
