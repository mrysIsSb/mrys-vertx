package top.mrys.vertx.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import cn.hutool.core.util.ArrayUtil;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

/**
 * 自定义 spring verticle factory
 * @author mrys
 * 2021/6/4
 */
@Component
public class CustomVerticleFactory implements VerticleFactory {

  private final String prefixName = "spring";
  @Autowired
  private ApplicationContext context;

  /**
   * 前缀
   *
   * @author mrys
   */
  @Override
  public String prefix() {
    return prefixName;
  }

  @Override
  public void createVerticle(String verticleName, ClassLoader classLoader, Promise<Callable<Verticle>> promise) {
    String name = VerticleFactory.removePrefix(verticleName);
    try {
      Object bean = context.getBean(name);
      if (bean instanceof Verticle) {
        promise.complete(() -> (Verticle) bean);
      } else {
        promise.complete();
      }
    } catch (Exception e) {
      promise.fail(e);
    }
  }

  public Set<String> getAllNames() {
    String[] vs = context.getBeanNamesForType(Verticle.class);
    if (ArrayUtil.isEmpty(vs)) {
      return null;
    }
    return Arrays
            .stream(vs).map(name -> prefixName + ":" + name)
            .collect(Collectors.toSet());
  }
}
