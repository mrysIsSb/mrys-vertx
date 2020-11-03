package top.mrys.vertx.common.factorys;

/**
 * 获取实例对象的工厂
 *
 * @author mrys
 * @date 2020/10/27
 */
public interface ObjectInstanceFactory {

  static ObjectInstanceFactory getDefault() {
    return new DefaultObjectInstanceFactory();
  }

  /**
   * 根据class 获取对应的实例方法
   *
   * @author mrys
   */
  <T> T getInstance(Class<T> clazz);
}
