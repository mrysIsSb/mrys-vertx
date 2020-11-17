package top.mrys.vertx.springboot.micro;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * @author mrys
 * @date 2020/11/16
 */
public class MicroClientPostProcessor implements BeanDefinitionRegistryPostProcessor {


  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    System.out.println("------------------2-----------------");
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    System.out.println("------------------1-----------------");
  }
}
