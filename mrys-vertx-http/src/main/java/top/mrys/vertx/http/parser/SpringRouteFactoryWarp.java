package top.mrys.vertx.http.parser;

import io.vertx.core.Vertx;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;
import top.mrys.vertx.http.annotations.RouteHandler;

/**
 * @author mrys
 * @date 2020/8/7
 */
@Component
@Slf4j
public class SpringRouteFactoryWarp extends DefaultRouteFactory {


  @Autowired
  private Vertx vertx;

  @Autowired
  private MyRefreshableApplicationContext context;

  @Override
  protected Object getControllerInstance(Class clazz) {
    try {
      return context.getBean(clazz);
    } catch (BeansException e) {
      log.error("获取bean失败",e);
    }
    return null;
  }

  @PostConstruct
  public void init() throws Exception {
    super.vertx=vertx;
    classes = context.getBeansWithAnnotation(RouteHandler.class).values().stream().map(Object::getClass)
        .distinct().collect(
        Collectors.toList());
    interceptors.add(new HttpInterceptor());
  }

}
