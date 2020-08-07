package top.mrys.vertx.common.launcher;

import io.vertx.core.Vertx;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.util.CollectionUtils;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Slf4j
public class MyRefreshableApplicationContext extends AbstractRefreshableApplicationContext {

  private final List<RegisterBean> registerBeans = Collections.synchronizedList(new ArrayList<>());

  private final List<String> packages = new ArrayList<>();

  @Override
  protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
      throws BeansException, IOException {
    AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
    registerBeans.forEach(registerBean -> registerBean.register(reader));
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanFactory);
    if (!CollectionUtils.isEmpty(packages)) {
      scanner.scan(packages.toArray(new String[]{}));
    }
  }

  public <T> void addBeanDefinition(RegisterBean<T> registerBean) {
    registerBeans.add(registerBean);
  }

  public void addScanPackage(String... packages) {
    if (packages != null) {
      this.packages.addAll(Arrays.asList(packages));
    }
  }

  public <T> void register(Class<T> clazz) {
    addBeanDefinition(new RegisterBean<>(clazz));
  }

  public <T> void registerBean(Class<T> tClass, Supplier<T> s) {
    registerBean(null, tClass, s);
  }

  public <T> void registerBeanByInstance(Class<T> tClass, T o) {
    registerBean(tClass, () -> o);
  }

  public <T> void registerBean(String name, Class<T> tClass, Supplier<T> s) {
    addBeanDefinition(new RegisterBean<>(tClass).setName(name).setSupplier(s));
  }

  public void removeBean(Class aClass) {
    ListIterator<RegisterBean> i = registerBeans.listIterator();
    for (; i.hasNext(); ) {
      RegisterBean next = i.next();
      if (next.getBeanClass().equals(aClass)) {
        i.remove();
      }
    }
  }

  /**
   * Prepare this context for refreshing, setting its startup date and active flag as well as
   * performing any initialization of property sources.
   */
  @Override
  protected void prepareRefresh() {
    if (isActive()) {
      try {
        Vertx bean = getBean(Vertx.class);
        bean.close();
      } catch (BeansException e) {
      }
    }
    super.prepareRefresh();
  }

  @Override
  public void refresh() throws BeansException, IllegalStateException {
    super.refresh();
  }

  public void refreshIfActive() {
    if (isActive()) {
      refresh();
    }
  }


  @Override
  protected void finishRefresh() {
    super.finishRefresh();
    publishEvent(new VertxStartedEvent(this));
  }
}
