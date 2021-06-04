package top.mrys.vertx.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;

/**
 * @author mrys
 * 2021/6/4
 */
@Component
public class CustomVerticleFactory implements VerticleFactory {

  @Autowired
  private ApplicationContext context;

  @Override
  public String prefix() {
    return "spring";
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
    return Arrays.stream(vs).map(s -> "spring" + ":" + s).collect(Collectors.toSet());
  }
}
