package top.mrys.vertx.common.launcher;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.BeanDefinitionCustomizer;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.lang.NonNull;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Getter
@Setter
@Accessors(chain = true)
public class RegisterBean<T> {

  @NonNull
  private Class<T> beanClass;
  private String name;
  private Class<? extends Annotation>[] qualifiers;
  private Supplier<T> supplier;
  private BeanDefinitionCustomizer[] customizers;

  private Consumer<AnnotatedBeanDefinitionReader> consumer ;

  public RegisterBean(@NonNull Class<T> beanClass) {
    this.beanClass = beanClass;
  }

  public RegisterBean<T> setQualifiers(Class<? extends Annotation>[] qualifiers) {
    this.qualifiers = qualifiers;
    consumer = reader -> reader.registerBean(this.beanClass, this.name, this.qualifiers);
    return this;
  }

  public RegisterBean<T> setSupplier(Supplier<T> supplier) {
    this.supplier = supplier;
    consumer = reader -> reader.registerBean(this.beanClass, this.name,this.supplier,this.customizers);
    return this;
  }

  public void register(AnnotatedBeanDefinitionReader reader) {
    if (Objects.isNull(consumer)) {
      reader.registerBean(this.beanClass, this.name);
    } else {
      consumer.accept(reader);
    }
  }
}
