package top.mrys.vertx.boot.config;


import org.springframework.context.annotation.Scope;
import top.mrys.vertx.springboot.http.server.EnableHttp;

/**
 * @author mrys
 * @date 2020/11/6
 */
@Scope
@EnableHttp(port = 8899, scanPackage = "top.mrys.vertx.boot.controller")
public class HttpConfig {

}
