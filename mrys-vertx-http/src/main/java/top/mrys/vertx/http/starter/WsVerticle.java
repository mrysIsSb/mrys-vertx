package top.mrys.vertx.http.starter;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.ServerWebSocket;
import lombok.Getter;
import lombok.Setter;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.http.parser.WebSocketHandlerFactory;

/**
 * @author mrys
 * @date 2020/10/27
 */
public class WsVerticle extends MyAbstractVerticle {

  @Getter
  @Setter
  private HttpServerOptions httpServerOptions = new HttpServerOptions();

  /**
   * 启动
   *
   * @author mrys
   */
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer(httpServerOptions)
        .webSocketHandler(handler())
        .listen(httpServerOptions.getPort(), event -> {
          if (event.succeeded()) {
            startPromise.complete();
          } else {
            startPromise.fail(event.cause());
          }
        });
  }

  private Handler<ServerWebSocket> handler() {
    return getWebSocketHandlerFactory().getHandler();
  }

  /**
   * 获取websockethandler 工厂
   *
   * @author mrys
   */
  private WebSocketHandlerFactory getWebSocketHandlerFactory() {
    WebSocketHandlerFactory instance = instanceFactory.getInstance(WebSocketHandlerFactory.class);
    if (instance == null) {
      instance = WebSocketHandlerFactory.getDefaultWebSocketHandlerFactory();
    }
    instance.setObjectInstanceFactory(instanceFactory);
    return instance;
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }
}
