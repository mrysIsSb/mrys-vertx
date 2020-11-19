package top.mrys.vertx.common.launcher;

import io.vertx.core.http.HttpServerOptions;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import top.mrys.vertx.common.factorys.DefaultObjectInstanceFactory;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;

/**
 * @author mrys
 * @date 2020/10/27
 */
public class MyVerticleFactory {

  static MyVerticleFactory def = new MyVerticleFactory();

  public static MyVerticleFactory getDefault() {
    return def;
  }

  /**
   * 对象实例化工厂
   *
   * @author mrys
   */
  @Setter
  @Getter
  private ObjectInstanceFactory instanceFactory = ObjectInstanceFactory.getDefault();

  @Setter
  @Getter
  private ApplicationContext context = new ApplicationContext();

  @Getter
  @Setter
  private HttpServerOptions httpServerOptions = new HttpServerOptions();

  /**
   * 获取verticle
   *
   * @author mrys
   */
  public <T extends MyAbstractVerticle> T getMyAbstractVerticle(Class<T> verticleClass) {
    T verticle = instanceFactory.getInstance(verticleClass);
    if (verticle.getContext() == null) {
      context.setVerticleFactory(this);
      verticle.setContext(context);
    }
    return verticle;
  }

  /**
   * 获取verticle 后续操作
   *
   * @author mrys
   */
  public <T extends MyAbstractVerticle> T getMyAbstractVerticle(Class<T> verticleClass,
      Function<T, T> process) {
    T verticle = getMyAbstractVerticle(verticleClass);
    return process.apply(verticle);
  }
}
