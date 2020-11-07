package top.mrys.vertx.boot;

import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.http.starter.EnableHttp;

/**
 * @author mrys
 * @date 2020/9/22
 */
@Slf4j
@SpringBootApplication
public class ConfigBoot {

  public static void main(String[] args) {
//    SpringApplication.run(ConfigBoot.class, args);

    Signaller signaller = new Signaller(true, 0L, 0L);
  }
}
