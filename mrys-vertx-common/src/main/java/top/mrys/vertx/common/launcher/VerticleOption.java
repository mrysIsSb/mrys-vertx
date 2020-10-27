package top.mrys.vertx.common.launcher;

import top.mrys.vertx.common.factorys.ObjectInstanceFactory;

/**
 * @author mrys
 * @date 2020/10/27
 */
public interface VerticleOption {

  /**
   * 设置对象实例化工厂
   *
   * @author mrys
   */
  default void setObjectInstanceFactory(ObjectInstanceFactory factory) {

  }

}
