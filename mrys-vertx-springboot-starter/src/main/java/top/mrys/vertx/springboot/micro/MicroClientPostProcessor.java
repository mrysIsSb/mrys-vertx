package top.mrys.vertx.springboot.micro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import top.mrys.vertx.common.spring.ConditionalOnBean;

/**
 * @author mrys
 * @date 2020/11/16
 */
@Slf4j
public class MicroClientPostProcessor implements BeanDefinitionRegistryPostProcessor {


  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    log.info("加工beanDefinition");
    String[] names = registry.getBeanDefinitionNames();
    for (String name : names) {
      BeanDefinition beanDefinition = registry.getBeanDefinition(name);
    }
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
  }
}
