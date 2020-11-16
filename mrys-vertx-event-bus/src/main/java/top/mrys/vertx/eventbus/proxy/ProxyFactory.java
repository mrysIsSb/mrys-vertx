package top.mrys.vertx.eventbus.proxy;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author mrys
 * @date 2020/11/11
 */
public interface ProxyFactory {

  /**
   * 创建代理对象
   *
   * @author mrys
   */
  <T> T getProxyInstance(Class<T> clazz);

  default boolean isDefaultMethod(Method method) {
    return (method.getModifiers()
        & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
        && method.getDeclaringClass().isInterface();
  }

  default Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
      throws Throwable {
    final Constructor<Lookup> constructor = MethodHandles.Lookup.class
        .getDeclaredConstructor(Class.class, int.class);
    if (!constructor.isAccessible()) {
      constructor.setAccessible(true);
    }
    final Class<?> declaringClass = method.getDeclaringClass();
    return constructor
        .newInstance(declaringClass,
            MethodHandles.Lookup.PRIVATE | MethodHandles.Lookup.PROTECTED
                | MethodHandles.Lookup.PACKAGE | MethodHandles.Lookup.PUBLIC)
        .unreflectSpecial(method, declaringClass).bindTo(proxy).invokeWithArguments(args);
  }
}
