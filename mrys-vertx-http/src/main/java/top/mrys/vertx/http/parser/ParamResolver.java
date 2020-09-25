package top.mrys.vertx.http.parser;

import io.vertx.ext.web.RoutingContext;

/**
 * @author mrys
 * @date 2020/9/22
 */
public interface ParamResolver extends Comparable<ParamResolver> {

  boolean match(HttpParamType type);

  <T> T resolve(HttpParamType<T> type, RoutingContext context);

  /**
   * 越大越优先
   * @author mrys
   */
  default int order() {
    return 0;
  }

  default int compareTo(ParamResolver o) {
    return Integer.compare(o.order(),order());
  }

}
