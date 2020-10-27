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
}
