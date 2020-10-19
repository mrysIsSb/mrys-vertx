package top.mrys.vertx.http.parser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.RoutingContext;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.HeaderVar;
import top.mrys.vertx.http.annotations.ReqBody;
import top.mrys.vertx.http.exceptions.HeaderVarRequiredException;

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
   * @author mrys
   */
  @Override
  public <T> Future<T> resolve(MethodParameter parameter, RoutingContext context) {
    ReqBody body = parameter.getParameterAnnotation(ReqBody.class);
    JsonTransverter bean = MyLauncher.context.getBean(JsonTransverter.class);
    return getFromBody(context).compose(buffer -> {
      T o = bean.deSerialize(buffer.toString(), parameter.getParameterClass());
      if (body.required() && BeanUtil.isEmpty(o)) {
        return Future.failedFuture(new NullPointerException());
      }
      return Future.succeededFuture(o);
    });
  }

}
