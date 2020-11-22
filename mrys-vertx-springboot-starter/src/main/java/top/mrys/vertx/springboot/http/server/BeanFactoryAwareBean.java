package top.mrys.vertx.springboot.http.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author mrys
 * @date 2020/11/5
 */
@Slf4j
public class BeanFactoryAwareBean implements ApplicationContextAware {


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    log.debug("{}", applicationContext.getClass().getName());
  }
}
