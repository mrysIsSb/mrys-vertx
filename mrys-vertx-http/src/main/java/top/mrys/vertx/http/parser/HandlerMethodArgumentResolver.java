package top.mrys.vertx.http.parser;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.other.MethodParameter;

/**
 * 解析获取方法参数
 *
 * @author mrys
 * @date 2020/9/22
 */
public interface HandlerMethodArgumentResolver extends Comparable<HandlerMethodArgumentResolver> {

  /**
   * 判断参数是否满足这个解析器 实现类重写
   *
   * @author mrys
   */
  default boolean match(MethodParameter parameter) {
    return false;
  }

  /**
   * 解析获取参数 实现类重写
   *
   * @return
   * @author mrys
   */
  default <T> T resolve(MethodParameter parameter, RoutingContext context) {
    return null;
  }

  /**
   * 在请求地址的参数中获取值
   *
   * @author mrys
   */
  default String getFromUrlParam(String name, RoutingContext context) {
    return context.request().getParam(name);
  }

  /**
   * 从请求地址中获取值
   *
   * @author mrys
   */
  default String getFromUrlPath(String name, RoutingContext context) {
    return context.pathParam(name);
  }

  /**
   * 从请求头中获取
   *
   * @author mrys
   */
  default String getFromHeader(String name, RoutingContext context) {
    return context.request().getHeader(name);
  }

  /**
   * 在reqbody回去数据
   *
   * @author mrys
   */
  default Buffer getFromBody(RoutingContext context) {
    return context.getBody();
  }


  /**
   * 越大越优先 权重吧
   *
   * @author mrys
   */
  default int order() {
    return 0;
  }

  default int compareTo(HandlerMethodArgumentResolver o) {
    return Integer.compare(o.order(), order());
  }

}
