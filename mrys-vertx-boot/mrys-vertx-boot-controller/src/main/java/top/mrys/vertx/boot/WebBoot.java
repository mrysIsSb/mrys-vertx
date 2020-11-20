package top.mrys.vertx.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mrys
 * @date 2020/9/8
 */
@Slf4j
@SpringBootApplication
public class WebBoot {

  public static void main(String[] args) {
    SpringApplication.run(WebBoot.class, args);
  }
}
