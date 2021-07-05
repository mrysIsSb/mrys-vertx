package top.mrys.vertx.springboot;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author mrys
 * @date 2021/7/5
 */
public abstract class CustomAbstractVerticle extends AbstractVerticle {

  @Autowired
  private ApplicationContext parent;

  protected AnnotationConfigApplicationContext sc;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    this.sc = new AnnotationConfigApplicationContext();
    sc.setParent(this.parent);
    sc.refresh();
  }
}
