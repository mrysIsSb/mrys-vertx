package top.mrys.vertx.ws.client.boot.config;

import top.mrys.vertx.springboot.http.server.EnableHttp;

/**
 * @author mrys
 * @date 2020/11/3
 */
@EnableHttp(scanPackage = "top.mrys.vertx.ws.client.boot.controller")
public class HttpConfig {

}
