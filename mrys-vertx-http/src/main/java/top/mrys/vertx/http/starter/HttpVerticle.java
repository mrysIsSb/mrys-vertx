package top.mrys.vertx.http.starter;

import cn.hutool.core.date.DateTime;
import io.netty.channel.group.DefaultChannelGroup;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocket;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.consts.ConstLog;

/**
 * @author mrys
 * @date 2020/9/12
 */
@Slf4j
public class HttpVerticle extends AbstractVerticle {

  private int port;
  private Handler<HttpServerRequest> router;
  private List<ServerWebSocket> wss=new ArrayList<>();

  public HttpVerticle(int port, Handler router) {
    this.port = port;
    this.router = router;
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServerOptions options = new HttpServerOptions();
    options.setLogActivity(true);
    vertx.createHttpServer(options)
        .webSocketHandler(this::getWebSocketHandler)
        .requestHandler(this.router)
        .listen(port)
        .onSuccess(event -> startPromise.complete())
        .onFailure(startPromise::fail);
  }

  private void getWebSocketHandler(ServerWebSocket webSocket) {
    webSocket.binaryHandlerID()
    System.out.println("");
/*    webSocket.closeHandler(event -> )
    DefaultChannelGroup*/
/*    Promise<Integer> promise = Promise.promise();
    webSocket.setHandshake(promise.future());*/
    /*authenticate(webSocket.headers(), ar -> {
      if (ar.succeeded()) {
        // Terminate the handshake with the status code 101 (Switching Protocol)
        // Reject the handshake with 401 (Unauthorized)
        promise.complete(ar.succeeded() ? 101 : 401);
      } else {
        // Will send a 500 error
        promise.fail(ar.cause());
      }
    });*/
  }


  @Override
  public void stop() throws Exception {
    log.info(ConstLog.log_template1, "http stop");
  }
}
