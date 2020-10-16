package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.exceptions.PathVarRequiredException;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class PathVarParamResolver implements ParamResolver {

  @Override
  public boolean match0(HttpParamType type) {
    return EnumParamFrom.PATH.equals(type.getFrom())
        && AnnotationUtil.isHaveAnyAnnotations(type.getAnnotation(), PathVar.class);
  }

  @Override
  public <T> Future<T> resolve(HttpParamType<T> type, RoutingContext context) {
    PathVar pathVar = AnnotationUtil.getAnnotation(type.getAnnotation(), PathVar.class);
    T result = Convert.convert(type.getClazz(),
        context.pathParam(StrUtil.isNotBlank(pathVar.value()) ? pathVar.value() :
            type.getName()));
    if (pathVar.required() && result == null) {
      return Future.failedFuture(new PathVarRequiredException());
    }
    return Future.succeededFuture(result);
  }

}
