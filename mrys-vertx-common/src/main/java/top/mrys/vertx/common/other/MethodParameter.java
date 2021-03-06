package top.mrys.vertx.common.other;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotationSelectors;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import top.mrys.vertx.common.utils.ASMUtil;
import top.mrys.vertx.common.utils.AnnotationUtil;

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
  @Getter
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
    if (ArrayUtil.isEmpty(parameterAnnotations)) {
      return null;
    }
    for (Annotation ann : parameterAnnotations) {
      if (annotationType.isInstance(ann)) {
        return (A) ann;
      }
    }
    return MergedAnnotations.from(parameterAnnotations)
        .get(annotationType, null, MergedAnnotationSelectors.firstDirectlyDeclared())
        .synthesize(MergedAnnotation::isPresent).orElse(null);
  }

  @SneakyThrows
  public static MethodParameter[] create(@NonNull Method method) {
    Parameter[] parameters = method.getParameters();
    MethodParameter[] result = new MethodParameter[parameters.length];
    if (ArrayUtil.isNotEmpty(parameters)) {
//      String[] paramNames = ASMUtil.getMethodParamNames(method);
      for (int i = 0; i < parameters.length; i++) {
        Parameter parameter = parameters[i];
        MethodParameter methodParameter = new MethodParameter();
        methodParameter.name =parameter.getName();/* StrUtil.blankToDefault(paramNames[i],parameter.getName());*/
        methodParameter.parameterClass = parameter.getType();
        methodParameter.parameterAnnotations = getMethodParametersAnnotations(i, parameter);
        methodParameter.parameterIndex = i;
        methodParameter.parameter = parameter;
        methodParameter.method = method;
        result[i] = methodParameter;
      }
    }
    return result;
  }

  /**
   * 获取方法参数上的注解或父接口上的
   *
   * @author mrys
   */
  protected static Annotation[] getMethodParametersAnnotations(int i, Parameter parameter) {
    Annotation[] paramAnns = parameter.getAnnotations();
    return ArrayUtil
        .append(paramAnns, AnnotationUtil.getInterfaceMethodParameterAnnotation(parameter, i));
  }
}
