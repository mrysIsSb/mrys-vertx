package top.mrys.vertx.eventbus;

import io.vertx.core.Vertx;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/9/9
 */
public class JdkHttpMicroClientFactoryBean<T> extends MicroClientFactoryBean<T> {

  @Override
  public T getObject() throws Exception {
    return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type},
        (proxy, method, args) -> {
          if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this,args);
          }
          if (AnnotationUtil.isHaveAnyAnnotations(method, RouteMapping.class)) {
            RouteMapping routeMapping = method.getAnnotation(RouteMapping.class);
            //todo
            return new Object();
          } else {
            if (isDefaultMethod(method)) {
              return invokeDefaultMethod(proxy, method, args);
            }
            throw new RuntimeException("异常");
          }
        });
  }
  private boolean isDefaultMethod(Method method) {
    return (method.getModifiers()
        & (Modifier.ABSTRACT | Modifier.PUBLIC | Modifier.STATIC)) == Modifier.PUBLIC
        && method.getDeclaringClass().isInterface();
  }
  private Object invokeDefaultMethod(Object proxy, Method method, Object[] args)
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
