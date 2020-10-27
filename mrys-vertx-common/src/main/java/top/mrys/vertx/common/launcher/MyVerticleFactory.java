package top.mrys.vertx.common.launcher;

import io.vertx.core.http.HttpServerOptions;
import lombok.Getter;
import lombok.Setter;
import top.mrys.vertx.common.factorys.DefaultObjectInstanceFactory;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;

/**
 * @author mrys
 * @date 2020/10/27
 */
public class MyVerticleFactory {

  /**
   * 对象实例化工厂
   *
   * @author mrys
   */
  @Setter
  @Getter
  private ObjectInstanceFactory instanceFactory = new DefaultObjectInstanceFactory();

  @Setter
  @Getter
  private ApplicationContext context = new ApplicationContext();

  @Getter
  @Setter
  private HttpServerOptions httpServerOptions = new HttpServerOptions();


  public <T extends MyAbstractVerticle> T getMyAbstractVerticle(Class<T> verticleClass) {
    T verticle = instanceFactory.getInstance(verticleClass);
    if (verticle.getContext() == null) {
      verticle.setContext(context);
    }
    return verticle;
  }

}
