package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.HeaderVar;
import top.mrys.vertx.http.exceptions.HeaderVarRequiredException;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class HeaderVarHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  /**
   * 判断参数是否满足这个解析器 满足条件 带headerVar 而且是原始数据类型
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    //todo 需要改进 满足多种类型
    return parameter.getParameterAnnotation(HeaderVar.class) != null;
  }

  /**
   * 解析获取参数
   *
   * @param parameter
   * @param context
   * @author mrys
   */
  @Override
  public <T> Future<T> resolve(MethodParameter parameter, RoutingContext context) {
    HeaderVar headerVar = parameter.getParameterAnnotation(HeaderVar.class);
    String value = getFromUrlPath(
        StrUtil.isNotBlank(headerVar.value()) ? headerVar.value() : parameter.getName(), context);
    if (headerVar.required() && StrUtil.isBlank(value)) {
      return Future.failedFuture(new HeaderVarRequiredException());
    }
    return Future.succeededFuture(Convert.convert(parameter.getParameterClass(), value));
  }
}
