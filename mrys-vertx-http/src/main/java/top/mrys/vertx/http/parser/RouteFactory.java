package top.mrys.vertx.http.parser;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.common.utils.Interceptor;

/**
 * @author mrys
 * @date 2020/7/4
 */
public interface RouteFactory<T extends RouteFactory> extends Supplier<Router> {

  /**
   * 添加要扫描的类
   *
   * @author mrys
   */
  T addClasses(Set<Class> classes);

  /**
   * 添加对象工厂实例
   *
   * @author mrys
   */
  T addObjectInstanceFactory(ObjectInstanceFactory factory);

  T addVertx(Vertx vertx);

  /**
   * 添加头拦截器
   *
   * @author mrys
   */
  T addFirst(Interceptor interceptor);

  /**
   * 添加最后一个拦截器
   *
   * @author mrys
   */
  T addLast(Interceptor interceptor);

  /**
   * Gets a result.
   *
   * @return a result
   */
  @Override
  Router get();
}
