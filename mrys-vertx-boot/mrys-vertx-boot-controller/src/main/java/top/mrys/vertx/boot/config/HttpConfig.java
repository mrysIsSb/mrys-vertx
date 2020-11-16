package top.mrys.vertx.boot.config;

import top.mrys.vertx.springboot.http.server.EnableHttp;

/**
 * @author mrys
 * @date 2020/11/14
 */
@EnableHttp(port = 8882,scanPackage = "top.mrys.vertx.boot.routes")
public class HttpConfig {

}
