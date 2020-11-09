package top.mrys.vertx.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.mrys.vertx.mysql.starter.EnableMysql;

/**
 * Config boot = ConfigFactory.parseFile(FileUtil.file("conf/boot.conf")); Config config =
 * ConfigFactory.parseFile(FileUtil.file("conf/config.json")); boot.withFallback(config);//合并，相同保留前
 * <p>
 * //    System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
 * Future<ApplicationContext> run = MyLauncher.run(SysBoot.class, args); run.onSuccess(event -> {
 * String[] names = event.getBeanDefinitionNames(); Arrays.stream(names).forEach(log::info);
 * log.info("{}",ConfigRepo.getInstance().getData()); });
 *
 * @author mrys
 * @date 2020/9/8
 */
@Slf4j
@SpringBootApplication
public class SysBoot {

  public static void main(String[] args) {
    SpringApplication.run(SysBoot.class, args);
  }
}
