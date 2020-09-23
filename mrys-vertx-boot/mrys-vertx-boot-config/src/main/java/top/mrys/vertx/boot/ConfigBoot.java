package top.mrys.vertx.boot;

import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.http.starter.EnableHttp;

/**
 * @author mrys
 * @date 2020/9/22
 */
@Slf4j
@EnableHttp(port = 8899,scanPackage = "top.mrys.vertx.boot.controller")
public class ConfigBoot {

  public static void main(String[] args) {
    MyLauncher.run(ConfigBoot.class, args).onSuccess(event -> log.info("启动成功"));
  }
}
