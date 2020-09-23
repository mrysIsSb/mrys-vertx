package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.PathVar;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class PathVarParamResolver implements ParamResolver {

  @Override
  public boolean match(HttpParamType type) {
    return EnumParamFrom.PATH.equals(type.getFrom());
  }

  @Override
  public <T> T resolve(HttpParamType<T> type, RoutingContext context) {
    PathVar pathVar = AnnotationUtil.getAnnotation(type.getAnnotation(), PathVar.class);
    return Convert.convert(type.getClazz(), StrUtil.isNotBlank(pathVar.value()) ? pathVar.value() :
        type.getName());
  }

}
