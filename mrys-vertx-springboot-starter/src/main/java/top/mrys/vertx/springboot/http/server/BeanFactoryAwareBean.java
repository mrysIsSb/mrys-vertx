package top.mrys.vertx.springboot.http.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author mrys
 * @date 2020/11/5
 */
public class BeanFactoryAwareBean implements ApplicationContextAware {


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    System.out.println(applicationContext);
  }
}
