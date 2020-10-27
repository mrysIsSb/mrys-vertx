package top.mrys.vertx.common.launcher;

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


  public <T extends MyAbstractVerticle> T getMyAbstractVerticle(Class<T> verticleClass) {
    T verticle = instanceFactory.getInstance(verticleClass);
    if (verticle.getInstanceFactory() == null) {
      verticle.setInstanceFactory(instanceFactory);
    }
    return verticle;
  }
}
