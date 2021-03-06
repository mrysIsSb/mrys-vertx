package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
import java.util.Map;
import top.mrys.vertx.common.factorys.JsonTransverterFactory;
import top.mrys.vertx.common.manager.EnumJsonTransverterNameProvider;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.common.utils.FutureUtil;

/**
 * 默认参数解析器
 *
 * @author mrys
 * @date 2020/9/22
 */
public class DefaultHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  /**
   * 判断参数是否满足这个解析器 实现类重写
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    return true;
  }

  /**
   * 解析获取参数 实现类重写 1.在请求地址参数中获取参数;2.请求地址;3.请求头；4.reqbody
   *
   * @param parameter
   * @param context
   * @return
   * @author mrys
   */
  @Override
  public <T> T resolve(MethodParameter parameter, RoutingContext context) {
    Class<T> parameterClass = parameter.getParameterClass();

    // 基本数据类型 或 string
    if (ClassUtil.isBasicType(parameterClass) || String.class.equals(parameterClass)) {
      return Convert.convert(parameterClass, getFromUrlParam(parameter.getName(), context));
    } else {
      String st = getFromBody(context).toString();
      //todo 判断数据类型
      T o = null;
      JsonTransverter bean = JsonTransverterFactory.getJsonTransverter(
          EnumJsonTransverterNameProvider.http_server);
      if (JSONUtil.isJson(st)) {
        o = bean.deSerialize(st, parameter.getParameterClass());
      }
      return o;
    }

  /*  FutureUtil<T> fu = FutureUtil.createInitFuture("初始化");
    // 基本数据类型
    if (ClassUtil.isBasicType(parameterClass) || String.class.equals(parameterClass)) {
      fu.nullOrFailedRecover(Future.succeededFuture(Convert.convert(parameter.getParameterClass(),
          getFromUrlParam(parameter.getName(), context))))
          .nullOrFailedRecover(Future.succeededFuture(Convert.convert(parameter.getParameterClass(),
              getFromUrlPath(parameter.getName(), context)
          )))
          .nullOrFailedRecover(Future.succeededFuture(Convert.convert(parameter.getParameterClass(),
              getFromHeader(parameter.getName(), context))))
          .nullOrFailedRecover(
              getFromBody(context).map(buffer -> {
                String v = buffer.toString();
                String header = context.request().getHeader(HttpHeaders.CONTENT_TYPE);
                if (StrUtil.isNotBlank(header) && header.toLowerCase()
                    .contains(HttpHeaderValues.APPLICATION_JSON.toLowerCase())) {
                  v = JSONUtil.parseObj(v).getStr(parameter.getName());
                }
                return Convert.convert(parameter.getParameterClass(), v);
              })
          );
    } else if (Map.class.isAssignableFrom(parameterClass)) {
      //map
      fu.nullOrFailedRecover(Future.succeededFuture());
    } else {
      fu.nullOrFailedRecover(Future.succeededFuture());
    }
    return fu.getFuture();*/
  }


  /**
   * 越大越优先 最后才执行
   *
   * @author mrys
   */
  @Override
  public int order() {
    return Integer.MIN_VALUE;
  }

}
