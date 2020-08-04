package top.mrys.vertx.common.launcher;

import java.lang.annotation.Annotation;
import lombok.Setter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import top.mrys.vertx.common.utils.TypeUtil;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Setter
public abstract class AbstractStarter<A extends Annotation> implements Starter<A>,
    ImportBeanDefinitionRegistrar, ApplicationListener<VertxStartedEvent> {

  private A a;

  @Override
  public void onApplicationEvent(VertxStartedEvent event) {
    start(a);
  }

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
      BeanDefinitionRegistry registry) {
    Class<A> annotationClass = (Class<A>) TypeUtil.getParameterizedType(this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
        .genericBeanDefinition(this.getClass());
    A annotation = importingClassMetadata.getAnnotations().get(annotationClass)
        .synthesize();
    beanDefinitionBuilder.addPropertyValue("a", annotation);
    registry.registerBeanDefinition(annotationClass.getSimpleName(), beanDefinitionBuilder.getBeanDefinition());
  }

}
