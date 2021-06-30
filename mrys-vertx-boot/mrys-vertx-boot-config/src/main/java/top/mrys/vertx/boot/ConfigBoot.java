package top.mrys.vertx.boot;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.slf4j.Log4jMarkerFactory;
import org.slf4j.Marker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mrys
 * @date 2020/9/22
 */
@Slf4j
@SpringBootApplication
public class ConfigBoot {

  public static void main(String[] args) {
    SpringApplication.run(ConfigBoot.class, args);
    Marker marker = new Log4jMarkerFactory().getMarker("marker-test");
    log.info(marker,"marker test");
  }
}
