package top.mrys.vertx.springboot.http.server;

import io.vertx.core.http.HttpServerOptions;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.launcher.ApplicationContext;
import top.mrys.vertx.http.starter.HttpVerticle;

/**
 * http Verticle factory bean
 *
 * @author mrys
 * @date 2021/1/5
 */
public class HttpVerticleFactory implements FactoryBean<HttpVerticle> {

  @Autowired
  private ApplicationContext context = new ApplicationContext();


  @Override
  public HttpVerticle getObject() throws Exception {
    HttpVerticle verticle = new HttpVerticle();
    if (verticle.getContext() == null) {
      verticle.setContext(context);
    }
    return verticle;
  }


  @Override
  public Class<?> getObjectType() {
    return HttpVerticle.class;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}
