package top.mrys.vertx.demo;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mrys.vertx.springboot.CustomAbstractVerticle;

/**
 * @author mrys
 * @date 2021/7/5
 */
@Component
@Slf4j
public class MyVerticle extends CustomAbstractVerticle {


  @Override
  public void start() throws Exception {
    Arrays.stream(sc.getBeanDefinitionNames()).forEach(log::info);
  }
}
