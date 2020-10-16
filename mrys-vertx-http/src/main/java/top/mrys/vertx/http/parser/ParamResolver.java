package top.mrys.vertx.http.parser;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

/**
 * @author mrys
 * @date 2020/9/22
 */
public interface ParamResolver extends Comparable<ParamResolver> {

  default boolean match(HttpParamType type) {
    if (EnumParamFrom.ANY.equals(type.getFrom())) {
      return true;
    }
    return match0(type);
  }

  boolean match0(HttpParamType type);

  <T> Future<T> resolve(HttpParamType<T> type, RoutingContext context);

  /**
   * 越大越优先
   *
   * @author mrys
   */
  default int order() {
    return 0;
  }

  default int compareTo(ParamResolver o) {
    return Integer.compare(o.order(), order());
  }

}
