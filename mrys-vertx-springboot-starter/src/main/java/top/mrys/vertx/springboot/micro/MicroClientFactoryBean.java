package top.mrys.vertx.springboot.micro;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import top.mrys.vertx.eventbus.proxy.ProxyFactory;

/**
 * @author mrys
 * @date 2020/11/20
 */
@Data
public class MicroClientFactoryBean implements FactoryBean<Object> {

  private Class type;

  private Class proxyFactoryClass;

  @Autowired
  private List<ProxyFactory> proxyFactorys;

  @Override
  public Object getObject() throws Exception {
    return proxyFactorys.stream()
        .filter(proxyFactory -> proxyFactoryClass.isAssignableFrom(proxyFactory.getClass()))
        .findFirst().get()
        .getProxyInstance(type);
  }

  @Override
  public Class getObjectType() {
    return type;
  }
}
