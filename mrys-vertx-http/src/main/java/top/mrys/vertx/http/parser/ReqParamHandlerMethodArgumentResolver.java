package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.ReqParam;
import top.mrys.vertx.http.exceptions.PathVarRequiredException;

/**
 * @author mrys
 * @date 2020/11/6
 */
public class ReqParamHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  /**
   * 判断参数是否满足这个解析器
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    return parameter.getParameterAnnotation(ReqParam.class) != null;
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
    ReqParam param = parameter.getParameterAnnotation(ReqParam.class);
    String name = StrUtil.isBlank(param.value()) ? parameter.getName() : param.value();
    String v = getFromUrlParam(
        name, context);
    if (StrUtil.isBlank(v) && StrUtil.isNotBlank(param.defValue())) {
      v = param.defValue();
    }
    if (StrUtil.isBlank(v) && param.required()) {
      return Future.failedFuture(new NullPointerException(name + "不能为空"));
    }
    return Future.succeededFuture(Convert.convert(parameter.getParameterClass(), v));
  }
}
