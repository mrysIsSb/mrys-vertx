package top.mrys.vertx.boot.config;

import top.mrys.vertx.springboot.http.server.EnableHttp;

/**
 * @author mrys
 * @date 2020/11/7
 */
@EnableHttp(port = 8080, scanPackage = "top.mrys.vertx.boot.service")
public class HttpConfig {

}
