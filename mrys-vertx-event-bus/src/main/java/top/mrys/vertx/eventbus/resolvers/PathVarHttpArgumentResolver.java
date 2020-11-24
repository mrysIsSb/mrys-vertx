package top.mrys.vertx.eventbus.resolvers;

import io.vertx.ext.web.client.HttpRequest;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.PathVar;

/**
 * @author mrys
 * @date 2020/11/24
 */
public class PathVarHttpArgumentResolver implements HttpArgumentResolver {

  /**
   * 判断参数是否满足这个解析器 实现类重写
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    return parameter.getParameterAnnotation(PathVar.class) != null;
  }

  /**
   * 将参数解析到http请求
   *
   * @param parameter
   * @param o
   * @param request
   * @author mrys
   */
  @Override
  public void resolver(MethodParameter parameter, Object o, HttpRequest request) {
  }
}
