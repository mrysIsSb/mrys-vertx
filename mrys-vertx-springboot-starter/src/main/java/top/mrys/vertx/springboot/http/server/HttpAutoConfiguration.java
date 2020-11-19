package top.mrys.vertx.springboot.http.server;

import cn.hutool.core.collection.CollectionUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import top.mrys.vertx.http.interceptor.AbstractHttpInterceptor;
import top.mrys.vertx.http.parser.RouteFactory;
import top.mrys.vertx.http.starter.HttpVerticle;

/**
 * @author mrys
 * @date 2020/11/3
 */
@ConditionalOnMissingBean
public class HttpAutoConfiguration {

  @Autowired(required = false)
  private List<AbstractHttpInterceptor> interceptors;

  /**
   * http route factory bean
   *
   * @author mrys
   */
  @Bean
  public RouteFactory routeFactoryWarp() {
    SpringRouteFactoryWarp routeFactory = new SpringRouteFactoryWarp();
    if (CollectionUtil.isNotEmpty(interceptors)) {
      interceptors.forEach(routeFactory::addLast);
    }
    return routeFactory;
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public HttpVerticle httpVerticle() {
    return new HttpVerticle();
  }

  @Bean
  public BeanFactoryAwareBean beanFactoryAwareBean() {
    return new BeanFactoryAwareBean();
  }
}
