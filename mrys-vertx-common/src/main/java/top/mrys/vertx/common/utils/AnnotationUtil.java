package top.mrys.vertx.common.utils;

import cn.hutool.core.util.ArrayUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import lombok.SneakyThrows;
import org.springframework.core.annotation.AnnotatedElementUtils;

/**
 * @author mrys
 * @date 2020/7/4
 */
public class AnnotationUtil {

  public static <A extends Annotation> Method[] getMethodByAnnotation(Class clazz,
      Class<A> aClass) {
    Method[] methods = clazz.getMethods();
    return Arrays.stream(methods)
        .filter(method -> !Object.class.equals(method.getDeclaringClass()))
        .filter(method -> isHaveAnyAnnotations(method, aClass))
        .toArray(Method[]::new);
  }

  /**
   * 是否存在任意的注解
   *
   * @author mrys
   */
  public static <A extends Annotation> boolean isHaveAnyAnnotations(
      AnnotatedElement annotatedElement, Class<A>... annotationClass) {
    for (Class<A> aClass : annotationClass) {
      if (AnnotatedElementUtils.hasAnnotation(annotatedElement, aClass)) {
        return true;
      }
    }
    return false;
  }

  public static <A extends Annotation> Boolean isHaveAnyAnnotations(Annotation[] as,
      Class<A>... annotationClass) {
    for (Annotation annotation : as) {
      for (Class<A> aClass : annotationClass) {
        if (annotation.annotationType().equals(aClass)) {
          return true;
        }
      }
    }
    return false;
  }

  public static <A extends Annotation> A getAnnotation(Annotation[] as,
      Class<A> annotationClass) {
    for (Annotation annotation : as) {
      if (annotation.annotationType().equals(annotationClass)) {
        return (A) annotation;
      }
    }
    return null;
  }

  public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement,
      Class<A> aClass) {
    if (isHaveAnyAnnotations(annotatedElement, aClass)) {
      return annotatedElement.getAnnotation(aClass);
    }
    return null;
  }

  /**
   * 获取当前和父的并且合并属性
   *
   * @author mrys
   */
  public static <A extends Annotation> A findMergedAnnotation(AnnotatedElement annotatedElement,
      Class<A> aClass) {
    return AnnotatedElementUtils.getMergedAnnotation(annotatedElement, aClass);
  }


  public static Annotation[] getInterfaceMethodParameterAnnotation(
      Parameter parameter, int index) {
    Executable declaringExecutable = parameter.getDeclaringExecutable();
    return searchParameterAnnotation(declaringExecutable.getDeclaringClass(),
        declaringExecutable.getName(), declaringExecutable.getParameterTypes(), index);
  }

  public static Annotation[] searchParameterAnnotation(Class clazz, String name,
      Class[] parameterTypes, int index) {
    Class<?>[] interfaces = clazz.getInterfaces();
    Annotation[] res = null;
    Method method = null;
    if (ArrayUtil.isNotEmpty(interfaces)) {
      for (Class<?> anInterface : interfaces) {
        try {
          method = anInterface
              .getMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
//          e.printStackTrace();
        }
        if (method != null) {
          res = method.getParameters()[index].getAnnotations();
          break;
        }
      }
    }
    if (method==null) {
      Class superclass = clazz.getSuperclass();
      if (superclass != null) {
        res = searchParameterAnnotation(superclass, name, parameterTypes, index);
      }
    }
    return res;
  }
}
