package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.ext.web.RoutingContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.common.utils.FutureUtil;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class HttpParameterFactory {

  /**
   * http处理方法
   *
   * @author mrys
   */
  private ControllerMethodWrap wrap;

  private HttpParameterFactory() {
  }

  public static HttpParameterFactory getInstance(ControllerMethodWrap wrap) {
    HttpParameterFactory factory = new HttpParameterFactory();
    factory.wrap = wrap;
    return factory;
  }

  public Object[] getHttpParameter(RoutingContext context) {
    List<HandlerMethodArgumentResolver> resolvers = getParamResolvers();
    MethodParameter[] methodParameters = wrap.getMethodParameters();
    if (ArrayUtil.isEmpty(methodParameters)) {
      return null;
    }
    Object[] objects = new Object[methodParameters.length];
    for (int i = 0; i < methodParameters.length; i++) {
      MethodParameter parameter = methodParameters[i];
      for (HandlerMethodArgumentResolver resolver : resolvers) {
        if (resolver.match(parameter)) {
          Object o;
          if ((o = resolver.resolve(parameter, context)) != null) {
            objects[i] = o;
            break;
          }
        }
      }
    }
    return objects;
  }

  protected List<HandlerMethodArgumentResolver> getParamResolvers() {
    Iterator<HandlerMethodArgumentResolver> paramResolvers = ServiceLoader.load(
        HandlerMethodArgumentResolver.class).iterator();
    ArrayList<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();
    paramResolvers.forEachRemaining(paramResolver -> resolvers.add(paramResolver));
    List<HandlerMethodArgumentResolver> sorted = resolvers.stream()
        .sorted().collect(Collectors.toList());
    return sorted;
  }


}