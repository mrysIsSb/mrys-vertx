package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class DefaultParamResolver implements ParamResolver {

  @Override
  public boolean match0(HttpParamType type) {
    return EnumParamFrom.PARAM.equals(type.getFrom());
  }

  @Override
  public <T> Future<T> resolve(HttpParamType<T> type, RoutingContext context) {
    return Future.succeededFuture(
        Convert.convert(type.getClazz(), context.request().getParam(type.getName())));
  }

}
