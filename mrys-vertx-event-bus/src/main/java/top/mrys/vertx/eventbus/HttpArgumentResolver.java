package top.mrys.vertx.eventbus;

import io.vertx.ext.web.client.HttpRequest;
import top.mrys.vertx.common.other.MethodParameter;

/**
 * http参数解析器
 *
 * @author mrys
 * @date 2020/10/19
 */
public interface HttpArgumentResolver extends Comparable<HttpArgumentResolver> {

  /**
   * 判断参数是否满足这个解析器 实现类重写
   *
   * @author mrys
   */
  default boolean match(MethodParameter parameter) {
    return false;
  }

  /**
   * 将参数解析到http请求
   *
   * @author mrys
   */
  <T> void resolver(MethodParameter parameter, T o, HttpRequest request);

  /**
   * 越大越优先
   *
   * @author mrys
   */
  default int order() {
    return 0;
  }

  default int compareTo(HttpArgumentResolver o) {
    return Integer.compare(o.order(), order());
  }
}
