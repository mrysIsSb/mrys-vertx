package top.mrys.vertx.springboot.http.server;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import top.mrys.vertx.http.annotations.RouteHandler;

/**
 * @author mrys
 * @date 2020/11/3
 */
public class RouteHandlerFilter implements Filter {

  @Override
  public FilterType type() {
    return null;
  }


  @Override
  public Class<?>[] value() {
    return new Class[0];
  }


  @Override
  public Class<?>[] classes() {
    return new Class[0];
  }


  @Override
  public String[] pattern() {
    return new String[0];
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    return RouteHandler.class;
  }
}
