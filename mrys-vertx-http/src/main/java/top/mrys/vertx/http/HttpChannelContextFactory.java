package top.mrys.vertx.http;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.impl.HttpServerConnection;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.impl.ConnectionBase;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import top.mrys.vertx.common.AbstractContextHandler;
import top.mrys.vertx.common.ChannelContextFactory;
import top.mrys.vertx.common.ProcessContext;

/**
 * @author mrys
 * @date 2021/7/11
 */
public class HttpChannelContextFactory implements ChannelContextFactory<HttpContext>,
    Handler<HttpServerRequest> {

  @Getter
  private Set<Object> controllers = new HashSet<>();

  private Router router;

  @Getter
  @Setter
  private JsonObject config = new JsonObject();


  public HttpChannelContextFactory init(Vertx vertx) {
    router = Router.router(vertx);
    if (config.getBoolean("enableCors", false)) {
      enableCorsSupport(router);
    }
    controllers.forEach(o -> {
      Class<?> aClass = o.getClass();
      for (Method method : aClass.getMethods()) {
        Path path = method.getAnnotation(Path.class);
        if (path == null) {
          continue;
        }
        router
            .route(HttpMethod.valueOf(path.method()), path.value())
            .handler(event -> {
//              ConnectionBase connection = (ConnectionBase) event.request().connection(); 可以获取channel
              HttpContext httpContext = get((ContextInternal) vertx.getOrCreateContext());
              httpContext.setRoutingContext(event);
              httpContext
              .getPipe()
              .addLast(new AbstractContextHandler() {
                @SneakyThrows
                @Override
                public void handle(ProcessContext context, Object data) {
                  method.invoke(o, data);
                  super.handle(context, data);
                }
              }).fireHandle(event);
        });
      }
    });
    return this;
  }

  @Override
  public HttpContext get(ContextInternal contextInternal) {
    return HttpContext.create(contextInternal, null);
  }


  @Override
  public void handle(HttpServerRequest event) {
    router.handle(event);
  }

  protected void enableCorsSupport(Router router) {
    Set<String> allowHeaders = new HashSet<>();
    allowHeaders.add("x-requested-with");
    allowHeaders.add("Access-Control-Allow-Origin");
    allowHeaders.add("origin");
    allowHeaders.add("Content-Type");
    allowHeaders.add("accept");
    // CORS support
    router.route().handler(CorsHandler.create("*")
        .allowedHeaders(allowHeaders)
        .addOrigin("*")
        .allowedMethod(HttpMethod.OPTIONS)
        .allowedMethod(HttpMethod.GET)
        .allowedMethod(HttpMethod.POST)
        .allowedMethod(HttpMethod.DELETE)
        .allowedMethod(HttpMethod.PATCH)
        .allowedMethod(HttpMethod.PUT)
    );
  }
}
