package top.mrys.vertx.ws.client.boot;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import top.mrys.vertx.http.parser.WebSocketHandlerFactory;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Component
@Slf4j
public class MyWSHandlerFactory implements WebSocketHandlerFactory {

  @Override
  public Logger getLogger() {
    return log;
  }

  @Override
  public Handler<Buffer> handler(ServerWebSocket socket) {
    return new Handler<Buffer>() {
      int i = 0;
      @Override
      public void handle(Buffer event) {
        if (i < 10) {
          i++;
          String s = event.toString();
          log.info("接收："+ s);
          socket.write(Buffer.buffer(s + i));
        }else {
          socket.close();
        }
      }
    };
  }
}
