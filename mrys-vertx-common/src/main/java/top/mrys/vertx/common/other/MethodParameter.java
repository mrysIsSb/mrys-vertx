package top.mrys.vertx.common.other;

import cn.hutool.core.util.ArrayUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.lang.Nullable;
import top.mrys.vertx.common.utils.ASMUtil;

/**
 * @author mrys
 * @date 2020/10/18
 */
@Data
public class MethodParameter {

  /**
   * 参数名称
   *
   * @author mrys
   */
  private String name;
  /**
   * 参数类型
   *
   * @author mrys
   */
  private Class parameterClass;

  public <T> Class<T> getParameterClass() {
    return parameterClass;
  }

  /**
   * 参数的注解
   *
   * @author mrys
   */
  private Annotation[] parameterAnnotations = new Annotation[0];

  /**
   * 该参数在方法的位置
   *
   * @author mrys
   */
  private int parameterIndex;

  protected MethodParameter() {
  }

  @Nullable
  public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
    Annotation[] anns = getParameterAnnotations();
    for (Annotation ann : anns) {
      if (annotationType.isInstance(ann)) {
        return (A) ann;
      }
    }
    return null;
  }

  @SneakyThrows
  public static MethodParameter[] create(@NonNull Method method) {
    Parameter[] parameters = method.getParameters();
    if (ArrayUtil.isNotEmpty(parameters)) {
      MethodParameter[] result = new MethodParameter[parameters.length];
      String[] paramNames = ASMUtil.getMethodParamNames(method);
      for (int i = 0; i < parameters.length; i++) {
        Parameter parameter = parameters[i];
        MethodParameter methodParameter = new MethodParameter();
        methodParameter.name = paramNames[i];
        methodParameter.parameterClass = parameter.getType();
        methodParameter.parameterAnnotations = parameter.getAnnotations();
        methodParameter.parameterIndex = i;
        result[i] = methodParameter;
      }
      return result;
    }
    return null;
  }
}
