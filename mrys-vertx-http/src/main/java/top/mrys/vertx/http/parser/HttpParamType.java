package top.mrys.vertx.http.parser;

import cn.hutool.core.convert.Convert;
import io.vertx.ext.web.RoutingContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import top.mrys.vertx.common.utils.ASMUtil;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.PathVar;

/**
 * @author mrys
 * @date 2020/9/22
 */
@Getter
@Setter
public class HttpParamType<T> {

  private EnumParamFrom from;

  private Class<T> clazz;

  private Annotation[] annotation;

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
    return EnumParamFrom.PARAM;
  }

  public T getValue(RoutingContext context) {
    Iterator<ParamResolver> iterator = ServiceLoader.load(ParamResolver.class).iterator();
    ArrayList<ParamResolver> resolvers = new ArrayList<>();
    iterator.forEachRemaining(paramResolver -> resolvers.add(paramResolver));
    return resolvers.stream().filter(paramResolver -> paramResolver.match(this))
        .sorted().findFirst().get().resolve(this,context);
  }
}
