package top.mrys.vertx.config.nacos;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;

/**
 * @author mrys
 * 2021/6/2
 */
public class MrysNacosConfigStore implements ConfigStore {

  private VertxInternal vertx;
  private JsonObject config;
  private AtomicReference<Buffer> last = new AtomicReference<>();
  private AtomicBoolean inited = new AtomicBoolean(false);
  private HttpClient httpClient;
  private RequestOptions requestOptions;

  public MrysNacosConfigStore(Vertx vertx, JsonObject config) {
    this.vertx = (VertxInternal) vertx;
    this.config = config;
    last.set(new JsonObject().toBuffer());
    this.httpClient = vertx.createHttpClient();
    this.requestOptions = new RequestOptions(config);
  }

  private void getData(Promise<Buffer> promise) {
    httpClient.request(requestOptions)
      .compose(HttpClientRequest::send)
      .compose(HttpClientResponse::body)
      .onSuccess(event -> last.set(event))
      .onComplete(promise);
  }


  @Override
  public Future<Buffer> get() {
    if (inited.get()) {
      Buffer buffer = last.get();
      ContextInternal context = vertx.getOrCreateContext();
      return context.succeededFuture(buffer != null ? buffer : Buffer.buffer("{}"));
    } else {
      Promise<Buffer> promise = Promise.promise();
      getData(promise);
      return promise.future();
    }
  }
}
