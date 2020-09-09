package top.mrys.vertx.eventbus;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author mrys
 * @date 2020/9/8
 */
@Data
public abstract class MicroClientFactoryBean<T> implements FactoryBean<T> {

  protected Class<T> type;

  @Override
  public Class<T> getObjectType() {
    return type;
  }

}
