package top.mrys.vertx.common.factorys;

/**
 * @author mrys
 * @date 2020/10/27
 */
public class DefaultObjectInstanceFactory implements ObjectInstanceFactory {

  /**
   * 根据class 获取对应的实例方法
   *
   * @param clazz
   * @author mrys
   */
  @Override
  public <T> T getInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
