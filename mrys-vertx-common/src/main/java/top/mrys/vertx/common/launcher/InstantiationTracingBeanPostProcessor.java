package top.mrys.vertx.common.launcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author mrys
 * @date 2020/9/15
 */
@Slf4j
@Component
public class InstantiationTracingBeanPostProcessor implements BeanPostProcessor {


  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    log.trace("name:{}-->{}",beanName,bean);
    return bean;
  }
}
