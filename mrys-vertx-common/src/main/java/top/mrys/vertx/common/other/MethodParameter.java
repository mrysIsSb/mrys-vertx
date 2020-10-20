package top.mrys.vertx.common.other;

import cn.hutool.core.util.ArrayUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
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

  /**
   * 参数
   *
   * @author mrys
   */
  private Parameter parameter;

  private Method method;

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
    MergedAnnotations.from(parameterAnnotations).get()
    return  AnnotatedElementUtils.findMergedAnnotation(parameter, annotationType);
//    return AnnotatedElementUtils.findMergedAnnotation(parameter, annotationType);
  }

  @SneakyThrows
  public static MethodParameter[] create(@NonNull Method method) {
    Parameter[] parameters = method.getParameters();
    MethodParameter[] result = new MethodParameter[parameters.length];
    if (ArrayUtil.isNotEmpty(parameters)) {
      String[] paramNames = ASMUtil.getMethodParamNames(method);
      for (int i = 0; i < parameters.length; i++) {
        Parameter parameter = parameters[i];
        MethodParameter methodParameter = new MethodParameter();
        methodParameter.name = paramNames[i];
        methodParameter.parameterClass = parameter.getType();
        methodParameter.parameterAnnotations = getMethodParametersAnnotations(i, parameter,
            methodParameter);
        methodParameter.parameterAnnotations = parameter.getAnnotations();
        methodParameter.parameterIndex = i;
        methodParameter.parameter = parameter;
        methodParameter.method = method;
        result[i] = methodParameter;
      }
    }
    return result;
  }

  //todo 去父类查询
  protected static Annotation[] getMethodParametersAnnotations(int i, Parameter parameter,
      MethodParameter methodParameter) {
    Annotation[] paramAnns = parameter.getAnnotations();
    if (ArrayUtil.isEmpty(paramAnns)) {
      Annotation[][] annotationArray = parameter.getDeclaringExecutable().getParameterAnnotations();
      int index = i;
      if (parameter.getDeclaringExecutable() instanceof Constructor &&
          ClassUtils.isInnerClass(parameter.getDeclaringExecutable().getDeclaringClass()) &&
          annotationArray.length == parameter.getDeclaringExecutable().getParameterCount() - 1) {
        // Bug in javac in JDK <9: annotation array excludes enclosing instance parameter
        // for inner classes, so access it with the actual parameter index lowered by 1
        index = i - 1;
      }
      paramAnns = (index >= 0 && index < annotationArray.length ? annotationArray[index]
          : new Annotation[0]);
      methodParameter.parameterAnnotations = paramAnns;
    }
    return paramAnns;
  }
}
