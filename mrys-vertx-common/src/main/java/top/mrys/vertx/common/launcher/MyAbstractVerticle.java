package top.mrys.vertx.common.launcher;

import io.vertx.core.AbstractVerticle;
import lombok.Getter;
import lombok.Setter;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;

/**
 * @author mrys
 * @date 2020/8/18
 */
public class MyAbstractVerticle extends AbstractVerticle {

  @Setter
  @Getter
  protected ApplicationContext context;

}
