package top.mrys.vertx.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;
import top.mrys.vertx.eventbus.proxy.HttpClientProxyFactory;
import top.mrys.vertx.eventbus.proxy.ProxyFactory;

/**
 * @author mrys
 * @date 2020/9/7
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MicroClient {

  @Deprecated
  Class<? extends MicroClientFactoryBean> factoryBeanClass() default JdkHttpMicroClientFactoryBean.class;

  Class<? extends ProxyFactory> proxyFactory() default HttpClientProxyFactory.class;

  ConfigProcess[] ConfigProcess() default {};

  @Retention(RetentionPolicy.RUNTIME)
  @Target({})
  @interface ConfigProcess {

    Class processClass();

    String[] args() default {};
  }
}
