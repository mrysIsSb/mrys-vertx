package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.exceptions.PathVarRequiredException;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class PathVarHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  /**
   * 判断参数是否满足这个解析器
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    //todo 需要改进 满足多种类型
    return parameter.getParameterAnnotation(PathVar.class) != null;
  }

  /**
   * 解析获取参数
   *
   * @param parameter
   * @param context
   * @return
   * @author mrys
   */
  @Override
  public <T> T resolve(MethodParameter parameter, RoutingContext context) {
    PathVar pathVar = parameter.getParameterAnnotation(PathVar.class);
    String value = getFromUrlPath(
        StrUtil.isNotBlank(pathVar.value()) ? pathVar.value() : parameter.getName(), context);
    if (StrUtil.isBlank(value) && StrUtil.isNotBlank(pathVar.defValue())) {
      value = pathVar.defValue();
    }
    if (pathVar.required() && StrUtil.isBlank(value)) {
      throw new PathVarRequiredException();
    }
    return Convert.convert(parameter.getParameterClass(), value);
  }
/* @Override
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
  }*/

}
