package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.HeaderVar;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.exceptions.PathVarRequiredException;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class HeaderVarParamResolver implements ParamResolver {

  @Override
  public boolean match0(HttpParamType type) {
    return EnumParamFrom.PATH.equals(type.getFrom())
        && AnnotationUtil.isHaveAnyAnnotations(type.getAnnotation(), HeaderVar.class);
  }

  @Override
  public <T> T resolve(HttpParamType<T> type, RoutingContext context) {
    HeaderVar headerVar = AnnotationUtil.getAnnotation(type.getAnnotation(), HeaderVar.class);
    T result = Convert.convert(type.getClazz(),
        context.request().getHeader(StrUtil.isNotBlank(headerVar.value()) ? headerVar.value() :
            type.getName()));
    if (headerVar.required() && result == null) {
      throw new PathVarRequiredException();
    }
    return result;
  }

}
