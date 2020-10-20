package top.mrys.vertx.eventbus;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpStatus;
import com.sun.org.apache.bcel.internal.generic.IfInstruction;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.common.utils.ASMUtil;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.common.utils.MyJsonUtil;
import top.mrys.vertx.common.utils.TypeUtil;
import top.mrys.vertx.http.RouteUtil;
import top.mrys.vertx.http.annotations.ReqBody;
import top.mrys.vertx.http.annotations.RouteMapping;

/**
 * @author mrys
 * @date 2020/9/9
 */
@Slf4j
public class JdkHttpMicroClientFactoryBean<T> extends MicroClientFactoryBean<T> {

  @Autowired
  private Vertx vertx;

  @Autowired
  private WebClient webClient;

  @Autowired
  private JsonTransverter jsonTransverter;

/*  @PostConstruct
  public void init() {
    webClient = WebClient.create(vertx, new WebClientOptions().setKeepAlive(true));
  }*/

  @Override
  public T getObject() throws Exception {
    return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type},
        getHandler());
  }

  private InvocationHandler getHandler() {
    return new JdkHttpMicroInvocationHandler(jsonTransverter);
  }

  class JdkHttpMicroInvocationHandler implements InvocationHandler {

    private JsonTransverter jsonTransverter;

    private List<HttpArgumentResolver> resolvers;

    private ParameterNameDiscoverer discoverer;

    public JdkHttpMicroInvocationHandler(JsonTransverter jsonTransverter) {
      this.jsonTransverter = jsonTransverter;
      this.resolvers = getParamResolvers();
      this.discoverer = new DefaultParameterNameDiscoverer();
    }

    protected List<HttpArgumentResolver> getParamResolvers() {
      Iterator<HttpArgumentResolver> paramResolvers = ServiceLoader.load(
          HttpArgumentResolver.class).iterator();
      ArrayList<HttpArgumentResolver> resolvers = new ArrayList<>();
      paramResolvers.forEachRemaining(paramResolver -> resolvers.add(paramResolver));
      List<HttpArgumentResolver> sorted = resolvers.stream()
          .sorted().collect(Collectors.toList());
      return sorted;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (Object.class.equals(method.getDeclaringClass())) {
        //Object 的方法
        return method.invoke(this, args);
      }
      if (isDefaultMethod(method)) {
        // defult 的方法
        return invokeDefaultMethod(proxy, method, args);
      } else {
        //http 请求 目前先 实现future todo 完善

        String requestURI = RouteUtil.getPath(method);
        HttpMethod httpMethod = RouteUtil.getMethod(method);
        HttpRequest<Buffer> request = webClient
            .request(httpMethod, SocketAddress.inetSocketAddress(8801, "localhost"),
                requestURI);
        MethodParameter[] parameters = MethodParameter.create(method);
        Object data = null;
        int type = 1;
        if (ArrayUtil.isNotEmpty(parameters)) {
          for (MethodParameter parameter : parameters) {
            resolvers.forEach(httpArgumentResolver -> {
              if (httpArgumentResolver.match(parameter)) {
                httpArgumentResolver
                    .resolver(parameter, args[parameter.getParameterIndex()], request);
              }
            });
            if (parameter.getParameterAnnotation(ReqBody.class) != null) {
              type = 2;
              data = args[parameter.getParameterIndex()];
            }
          }
        }
        return send(request, method, type, data);
      }
    }

    /**
     * 返回类型为future
     *
     * @author mrys
     */
    private <T> Future send(HttpRequest<Buffer> request, Method method, Integer type, T sendData) {
      Promise promise = Promise.promise();
      Handler<AsyncResult<HttpResponse<Buffer>>> asyncResultHandler = getAsyncResultHandler(method,
          promise);
      if (0 == type) {
        //不求返回
        promise.complete();
        request.send();
      } else if (1 == type) {
        request.send(asyncResultHandler);
      } else if (2 == type) {
        // request body json
        request.sendJson(sendData, asyncResultHandler);
      }
      return promise.future();
    }

    private Handler<AsyncResult<HttpResponse<Buffer>>> getAsyncResultHandler(Method method,
        Promise promise) {
      return event -> {
        if (event.succeeded()) {
          HttpResponse<Buffer> result = event.result();
          if (result.statusCode() == HttpStatus.HTTP_OK) {
            Type returnType = method.getReturnType();
            if (returnType.equals(Future.class)) {
              Type[] actualTypeArguments = ((ParameterizedType) method
                  .getGenericReturnType())
                  .getActualTypeArguments();
              if (actualTypeArguments.length == 1) {
                returnType = actualTypeArguments[0];
              }
            }
            promise.complete(jsonTransverter
                .deSerialize(result.bodyAsString(), TypeUtil.getRawType(returnType)));
          } else {
            promise.fail(result.bodyAsString());
          }
        } else {
          promise.fail(event.cause());
        }
      };
    }


  }

}
