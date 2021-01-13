package top.mrys.vertx.http.parser;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.factorys.JsonTransverterFactory;
import top.mrys.vertx.common.manager.EnumJsonTransverterNameProvider;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.ReqBody;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class ReqBodyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  /**
   * 判断参数是否满足这个解析器
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    //todo 需要改进 满足多种类型
    return parameter.getParameterAnnotation(ReqBody.class) != null;
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
    ReqBody body = parameter.getParameterAnnotation(ReqBody.class);
    JsonTransverter bean = JsonTransverterFactory.getJsonTransverter(
        EnumJsonTransverterNameProvider.http_server);
    String st = getFromBody(context).toString();
    //todo 判断数据类型
    if (body.required() && StrUtil.isBlank(st)) {
      throw new NullPointerException();
    }
    T o = null;
    if (JSONUtil.isJson(st)) {
      o = bean.deSerialize(st, parameter.getParameterClass());
    }
    return o;

  }

}
