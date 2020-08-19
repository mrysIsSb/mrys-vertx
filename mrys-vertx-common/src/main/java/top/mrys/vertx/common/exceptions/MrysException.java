package top.mrys.vertx.common.exceptions;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import top.mrys.vertx.common.utils.AnnotationUtil;

/**
 * @author mrys
 * @date 2020/8/19
 */
public class MrysException extends RuntimeException {


  public MrysException(EnumException e) {
    super(e.getMsg());
  }

  public MrysException() {
  }
  public MrysException(String message) {
    super(message);
  }
  public MrysException(String message, Throwable cause) {
    super(message, cause);
  }
  public MrysException(Throwable cause) {
    super(cause);
  }
  public MrysException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  protected MrysException removeStackTraceElement() {
    StackTraceElement[] trace = getStackTrace();
    ClassLoader classLoader = this.getClass().getClassLoader();
    List<StackTraceElement> as = Arrays.stream(trace).filter(stackTraceElement -> {
      String className = stackTraceElement.getClassName();
      Class<?> aClass = null;
      try {
        aClass = classLoader.loadClass(className);
      } catch (ClassNotFoundException e) {
        return true;
      }
      String methodName = stackTraceElement.getMethodName();
      boolean remove = AnnotationUtil.isHaveAnyAnnotations(aClass, NoLog.class);
      Method[] method = AnnotationUtil.getMethodByAnnotation(aClass, NoLog.class);
      if (ArrayUtil.isNotEmpty(method) && !remove) {
        for (Method method1 : method) {
          if (methodName.equals(method1.getName())) {
            remove = true;
            break;
          }
        }
      }
      return !remove;
    }).collect(Collectors.toList());
    setStackTrace(as.toArray(new StackTraceElement[]{}));
    return this;
  }
}
