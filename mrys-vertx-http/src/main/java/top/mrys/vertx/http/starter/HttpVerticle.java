package top.mrys.vertx.http.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.ServerWebSocket;
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

  public HttpVerticle(int port, Handler router) {
    this.port = port;
    this.router = router;
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    HttpServerOptions options = new HttpServerOptions();
    options.setLogActivity(true);
    vertx.createHttpServer(options)
        .requestHandler(this.router)
        .listen(port)
        .onSuccess(event -> startPromise.complete())
        .onFailure(startPromise::fail);
  }

  private void getWebSocketHandler(ServerWebSocket webSocket) {
//    webSocket.binaryHandlerID()
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
