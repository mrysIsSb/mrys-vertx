package top.mrys.vertx.common.factorys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * 通过spring 来获取 实例对象
 *
 * @author mrys
 * @date 2020/10/27
 */
public class SpringObjectInstanceFactory implements ObjectInstanceFactory {


  @Autowired
  private ApplicationContext context;

  /**
   * 根据class 获取对应的实例方法
   *
   * @param clazz
   * @author mrys
   */
  @Override
  public <T> T getInstance(Class<T> clazz) {
    return context.getBean(clazz);
  }
}
