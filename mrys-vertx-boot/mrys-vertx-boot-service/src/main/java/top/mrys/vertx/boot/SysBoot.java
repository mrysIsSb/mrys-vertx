package top.mrys.vertx.boot;

import cn.hutool.core.io.FileUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.Future;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.eventbus.EnableMicroClient;
import top.mrys.vertx.http.starter.EnableHttp;
import top.mrys.vertx.mysql.starter.EnableMysql;

/**
 * @author mrys
 * @date 2020/9/8
 */
@EnableMysql
@EnableHttp(port = 8801, scanPackage = "top.mrys.vertx.boot.service")
@EnableMicroClient("top.mrys.vertx.boot.api")
@ComponentScan
@Slf4j
public class SysBoot {

  public static void main(String[] args) {

    Config boot = ConfigFactory.parseFile(FileUtil.file("conf/boot.conf"));
    Config config = ConfigFactory.parseFile(FileUtil.file("conf/config.json"));
    boot.withFallback(config);//合并，相同保留前

//    System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    Future<ApplicationContext> run = MyLauncher.run(SysBoot.class, args);
/*    run.onSuccess(event -> {
      String[] names = event.getBeanDefinitionNames();
      Arrays.stream(names).forEach(log::info);
      log.info("{}",ConfigRepo.getInstance().getData());
    });*/
  }
}
