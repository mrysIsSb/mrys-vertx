package top.mrys.vertx.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mrys
 * @date 2020/9/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroClient {

  Class<? extends MicroClientFactoryBean> factoryBeanClass() default JdkHttpMicroClientFactoryBean.class;
}
