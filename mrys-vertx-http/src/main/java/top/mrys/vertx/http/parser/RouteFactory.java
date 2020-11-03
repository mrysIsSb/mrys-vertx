package top.mrys.vertx.http.parser;

import io.vertx.ext.web.Router;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;

/**
 * @author mrys
 * @date 2020/7/4
 */
public interface RouteFactory extends Supplier<Router> {

  /**
   * 添加要扫描的类
   *
   * @author mrys
   */
  void addClasses(Set<Class> classes);

  /**
   * 添加对象工厂实例
   *
   * @author mrys
   */
  void addObjectInstanceFactory(ObjectInstanceFactory factory);

}
