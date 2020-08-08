package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

/**
 * @author mrys
 * @date 2020/8/8
 */
public class EmptyConfigStore implements ConfigStore {


  @Override
  public void get(Handler<AsyncResult<Buffer>> completionHandler) {
    Future<Buffer> future = Future.succeededFuture(new JsonObject().toBuffer());
    completionHandler.handle(future);
  }
}
