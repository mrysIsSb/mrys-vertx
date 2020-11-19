package top.mrys.vertx.eventbus.proxy;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.factorys.JsonTransverterFactory;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
import top.mrys.vertx.common.manager.EnumJsonTransverterNameProvider;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.common.utils.TypeUtil;
import top.mrys.vertx.eventbus.MicroClient;
import top.mrys.vertx.eventbus.MicroClient.ConfigProcess;
import top.mrys.vertx.eventbus.resolvers.HttpArgumentResolver;
import top.mrys.vertx.http.RouteUtil;
import top.mrys.vertx.http.annotations.ReqBody;

/**
 * http client proxy
 *
 * @author mrys
 * @date 2020/11/11
 */
@Slf4j
public class HttpClientProxyFactory implements ProxyFactory, InvocationHandler {

  private JsonTransverter jsonTransverter = JsonTransverterFactory
      .getJsonTransverter(EnumJsonTransverterNameProvider.http_server);
  @Getter
  @Setter
  private ObjectInstanceFactory objectInstanceFactory = ObjectInstanceFactory.getDefault();

  protected List<HttpArgumentResolver> getParamResolvers() {
    Iterator<HttpArgumentResolver> paramResolvers = ServiceLoader.load(
        HttpArgumentResolver.class).iterator();
    ArrayList<HttpArgumentResolver> resolvers = new ArrayList<>();
    paramResolvers.forEachRemaining(paramResolver -> resolvers.add(paramResolver));
    List<HttpArgumentResolver> sorted = resolvers.stream()
        .sorted().collect(Collectors.toList());
    return sorted;
  }

  /**
   * 创建代理对象
   *
   * @param clazz
   * @author mrys
   */
  @Override
  public <T> T getProxyInstance(Class<T> clazz) {
    Assert.notNull(clazz, "被代理class不能为空");
    Assert.isTrue(clazz.isInterface(),
        String.format("%s 非interface类型", clazz.getName()));
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    return (T) Proxy.newProxyInstance(loader, new Class[]{clazz}, this);
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    //Object 的方法
    if (Object.class.equals(method.getDeclaringClass())) {
      throw new RuntimeException("Object 方法不能调用");
      //return method.invoke(this, args);
    }
    // default 的方法
    if (isDefaultMethod(method)) {
      return invokeDefaultMethod(proxy, method, args);
    } else {
      //http 请求 目前先 实现future todo 完善

      String requestURI = RouteUtil.getPath(method)[0];
      HttpMethod httpMethod = RouteUtil.getMethod(method);
      //获取webclient
      WebClient webClient = objectInstanceFactory.getInstance(WebClient.class);
      MicroClient microClient = AnnotationUtil
          .findMergedAnnotation(method.getDeclaringClass(), MicroClient.class);
      ConfigProcess[] configProcesses = microClient.ConfigProcess();
      ConfigProcess webClientProcess = getConfigProcess(configProcesses, WebClientProcess.class);
      WebClientProcess wprocess = (WebClientProcess) objectInstanceFactory
          .getInstance(webClientProcess.processClass());
      HttpRequest<Buffer> request = wprocess
          .process(webClient, httpMethod, requestURI, webClientProcess.args());
      //请求参数
      MethodParameter[] parameters = MethodParameter.create(method);
      Object data = null;
      int type = 1;
      if (ArrayUtil.isNotEmpty(parameters)) {
        List<HttpArgumentResolver> resolvers = getParamResolvers();
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
      Class returnType = method.getReturnType();
      if (returnType.isAssignableFrom(Void.TYPE)) {
        type = 0;
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
      request.sendBuffer(Buffer.buffer(jsonTransverter.serialize(sendData)), asyncResultHandler);
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

  @SneakyThrows
  private ConfigProcess getConfigProcess(ConfigProcess[] configProcesses, Class clazz) {
    ConfigProcess result = null;
    if (ArrayUtil.isNotEmpty(configProcesses)) {
      result = Arrays.stream(configProcesses)
          .filter(configProcess -> clazz.isAssignableFrom(configProcess.processClass())).findFirst()
          .orElse(result);
    }
    if (result == null) {
      //todo
    }
    return result;
  }
}
