package top.mrys.vertx.common.factorys;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 通过spring 来获取 实例对象
 *
 * @author mrys
 * @date 2020/10/27
 */
public class SpringObjectInstanceFactory implements ObjectInstanceFactory, ApplicationContextAware {


  private ApplicationContext context;

  /**
   * 根据class 获取对应的实例方法
   *
   * @param clazz
   * @author mrys
   */
  @Override
  public <T> T getInstance(Class<T> clazz) {
    T t;
    try {
      t = context.getBean(clazz);
    } catch (NoSuchBeanDefinitionException e) {
      t = ObjectInstanceFactory.getDefault().getInstance(clazz);
    }
    return t;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }
}
