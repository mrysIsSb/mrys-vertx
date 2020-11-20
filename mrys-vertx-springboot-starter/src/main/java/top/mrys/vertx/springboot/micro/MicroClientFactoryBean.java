package top.mrys.vertx.springboot.micro;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.eventbus.proxy.ProxyFactory;

/**
 * @author mrys
 * @date 2020/11/20
 */
public class MicroClientFactoryBean implements FactoryBean<Object> {

  protected Class type;

  protected Class proxyFactoryClass;

  @Autowired
  protected Map<Class, ProxyFactory> proxyFactorys;

  @Override
  public Object getObject() throws Exception {
    return proxyFactorys.get(proxyFactoryClass).getProxyInstance(type);
  }

  @Override
  public Class getObjectType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public void setProxyFactoryClass(Class proxyFactoryClass) {
    this.proxyFactoryClass = proxyFactoryClass;
  }
}
