package top.mrys.vertx.eventbus;

import cn.hutool.http.HttpStatus;
import com.sun.org.apache.bcel.internal.generic.IfInstruction;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import top.mrys.vertx.common.utils.ASMUtil;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.common.utils.MyJsonUtil;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/9/9
 */
@Slf4j
public class JdkHttpMicroClientFactoryBean<T> extends MicroClientFactoryBean<T> {

  @Autowired
  private Vertx vertx;

  private WebClient webClient;

  @PostConstruct
  public void init() {
    webClient = WebClient.create(vertx, new WebClientOptions().setKeepAlive(true));
  }

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

        HttpRequest<Buffer> request = webClient
            .request(routeMapping.method().getHttpMethod(),
                SocketAddress.inetSocketAddress(8801, "localhost"),
                requestURI);
        //todo 完善 pathvar 等
        String[] names = new DefaultParameterNameDiscoverer().getParameterNames(method);
        for (int i = 0; i < parameters.length; i++) {
          request.addQueryParam(names[i], args[i].toString());
        }
        request
//            .addQueryParam()//添加可重复
//            .setQueryParam()//设置不重复
            .send(event -> {
              if (event.succeeded()) {
                HttpResponse<Buffer> result = event.result();
                if (result.statusCode()== HttpStatus.HTTP_OK) {
                  Type returnType = method.getReturnType();
                  if (returnType.equals(Future.class)) {
                    Type[] actualTypeArguments = ((ParameterizedType) method.getGenericReturnType())
                        .getActualTypeArguments();
                    if (actualTypeArguments.length == 1) {
                      returnType = actualTypeArguments[0];
                    }
                  }
                  promise.complete(MyJsonUtil.mapTo(result.bodyAsString(),returnType));
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

}