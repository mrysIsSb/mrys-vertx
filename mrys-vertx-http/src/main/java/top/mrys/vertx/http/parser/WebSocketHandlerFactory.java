package top.mrys.vertx.http.parser;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.mrys.vertx.common.launcher.VerticleOption;

/**
 * @author mrys
 * @date 2020/10/27
 */
public interface WebSocketHandlerFactory extends VerticleOption {

  /**
   * 默认
   *
   * @author mrys
   */
  static WebSocketHandlerFactory getDefaultWebSocketHandlerFactory() {
    return new WebSocketHandlerFactory() {
      @Override
      public Handler<ServerWebSocket> getHandler() {
        return event -> event.close();
      }
    };
  }

  default Handler<ServerWebSocket> getHandler() {
    return serverWebSocket -> {
      serverWebSocket.setHandshake(handshake(serverWebSocket));
      serverWebSocket.exceptionHandler(exceptionHandler(serverWebSocket));
      serverWebSocket.handler(handler(serverWebSocket));
    };
  }

  default Handler<Buffer> handler(ServerWebSocket socket) {
    return buffer -> {
      getLogger().info("接收："+buffer.toString(StandardCharsets.UTF_8));
    };
  }

  default Logger getLogger() {
    return LoggerFactory.getLogger(WebSocketHandlerFactory.class);
  }


  /**
   * 握手 成功返回 code：101
   *
   * @author mrys
   */
  default Future<Integer> handshake(ServerWebSocket socket) {
    return Future.succeededFuture(101);
  }


  default Handler<Throwable> exceptionHandler(ServerWebSocket socket) {
    return event -> event.printStackTrace();
  }
}
