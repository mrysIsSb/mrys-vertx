package top.mrys.vertx.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.eventbus.EnableMicroClient;
import top.mrys.vertx.http.starter.EnableHttp;

/**
 * @author mrys
 * @date 2020/9/8
 */
@EnableHttp(port = 8888, scanPackage = "top.mrys.vertx.boot.routes")
@EnableMicroClient("top.mrys.vertx.boot.api")
@ComponentScan
@Slf4j
public class WebBoot {

  public static void main(String[] args) {
    ApplicationContext run = MyLauncher.run(WebBoot.class, args);
    for (String name : run.getBeanDefinitionNames()) {
      System.out.println(name);
    }
  }
}
