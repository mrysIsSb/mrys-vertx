package top.mrys.vertx.boot.config;


import top.mrys.vertx.springboot.http.server.EnableHttp;

/**
 * @author mrys
 * @date 2020/11/6
 */
@EnableHttp(port = 8899, scanPackage = "top.mrys.vertx.boot.controller")
public class HttpConfig {

}
