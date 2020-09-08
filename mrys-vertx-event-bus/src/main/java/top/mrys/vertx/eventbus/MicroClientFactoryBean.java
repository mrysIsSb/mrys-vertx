package top.mrys.vertx.eventbus;

import cn.hutool.aop.proxy.JdkProxyFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author mrys
 * @date 2020/9/8
 */
@Data
public class MicroClientFactoryBean<T> implements FactoryBean<T> {
  private Class<T> type;

  private InvocationHandler handler;

  @Override
  public T getObject() throws Exception {
    return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type}, handler);
  }

  @Override
  public Class<T> getObjectType() {
    return type;
  }
}
