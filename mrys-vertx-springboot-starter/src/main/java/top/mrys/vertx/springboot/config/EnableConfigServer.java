package top.mrys.vertx.springboot.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.mrys.vertx.springboot.http.server.HttpAutoConfiguration;
import top.mrys.vertx.springboot.http.server.HttpStarter;

/**
 * @author mrys
 * @date 2020/11/19
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({HttpStarter.class, HttpAutoConfiguration.class})
@Configuration
public @interface EnableConfigServer {

}
