package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import io.vertx.ext.web.RoutingContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import top.mrys.vertx.common.utils.ASMUtil;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.HeaderVar;
import top.mrys.vertx.http.annotations.PathVar;

/**
 * @author mrys
 * @date 2020/9/22
 */
@Getter
@Setter
public class HttpParamType<T> {

  private EnumParamFrom from;
  /**
   * 参数类型
   *
   * @author mrys
   */
  private Class<T> clazz;

  /**
   * 参数注解
   *
   * @author mrys
   */
  private Annotation[] annotation;

  /**
   * 参数名称
   *
   * @author mrys
   */
  private String name;

  private HttpParamType() {
  }

  @SneakyThrows
  public static HttpParamType getInstance(Class clazz, Method method, Parameter param, int index) {
    String[] names = ASMUtil.getMethodParamNames(method);
    HttpParamType paramType = new HttpParamType();
    paramType.setClazz(param.getType());
    paramType.setName(names[index]);
    paramType.setFrom(getFrom(param));
    paramType.setAnnotation(param.getAnnotations());
    return paramType;
  }

  private static EnumParamFrom getFrom(Parameter param) {
    if (AnnotationUtil.isHaveAnyAnnotations(param, PathVar.class)) {
      return EnumParamFrom.PATH;
    }
    if (AnnotationUtil.isHaveAnyAnnotations(param, HeaderVar.class)) {
      return EnumParamFrom.HEADER;
    }
    return EnumParamFrom.ANY;
  }

  public T getValue(RoutingContext context) {
    Iterator<ParamResolver> paramResolvers = ServiceLoader.load(ParamResolver.class).iterator();
    ArrayList<ParamResolver> resolvers = new ArrayList<>();
    paramResolvers.forEachRemaining(paramResolver -> resolvers.add(paramResolver));
    List<ParamResolver> resolverList = resolvers.stream()
        .filter(paramResolver -> paramResolver.match(this))
        .sorted().collect(Collectors.toList());
    T result = null;
    for (ParamResolver resolver : resolverList) {
      T t = resolver.resolve(this, context);
      //解析的数据不为空和预返回数据为空
      if (t != null && result == null) {
        result = t;
      }
    }
    return result;
  }
}
