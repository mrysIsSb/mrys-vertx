package top.mrys.vertx.eventbus;

import cn.hutool.http.HttpStatus;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/9/9
 */
@Slf4j
public class JdkHttpMicroClientFactoryBean<T> extends MicroClientFactoryBean<T> {

  @Override
  public T getObject() throws Exception {
    return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type},
        getHandler());
  }

  private InvocationHandler getHandler() {
    return (proxy, method, args) -> {
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);
      }
      String parent = "";
      RouteMapping annotation = AnnotationUtil.getAnnotation(type, RouteMapping.class);
      if (annotation != null) {
        parent = annotation.value();
      }
      RouteMapping routeMapping = AnnotationUtil.getAnnotation(method, RouteMapping.class);
      if (routeMapping != null) {
        Promise promise = Promise.promise();
        String requestURI = parent + routeMapping.value();
        Parameter[] parameters = method.getParameters();
        HttpRequest<Buffer> request = WebClient.create(Vertx.vertx())
            .request(routeMapping.method().getHttpMethod(),
                SocketAddress.inetSocketAddress(8801, "localhost"),
                requestURI);
        //todo 完善
        for (int i = 0; i < parameters.length; i++) {
          request.addQueryParam(parameters[i].getName(), args[i].toString());
        }
        request
//            .addQueryParam()//添加可重复
//            .setQueryParam()//设置不重复
            .send(event -> {
              if (event.succeeded()) {
                HttpResponse<Buffer> result = event.result();
                if (result.statusCode()== HttpStatus.HTTP_OK) {
                  Class<?> returnType = method.getReturnType();
                  promise.complete(result.bodyAsJson(returnType));
                }else {
                  promise.fail(result.bodyAsString());
                }
              } else {
                promise.fail(event.cause());
              }
            });
        //todo
        return promise.future();
      } else {
        if (isDefaultMethod(method)) {
          return invokeDefaultMethod(proxy, method, args);
        }
        throw new RuntimeException("异常");
      }
    };
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
