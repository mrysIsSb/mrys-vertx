package top.mrys.vertx.common.config;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;

/**
 * @author mrys
 * @date 2020/9/18
 */
public class MyHttpConfigStore implements ConfigStore {

  private final HttpClient client;

  private final RequestOptions options;

  public MyHttpConfigStore(Vertx vertx, JsonObject json) {
    this.client = vertx.createHttpClient(new HttpClientOptions(json));
    this.options = createRequestOptions(json);
  }

  private RequestOptions createRequestOptions(JsonObject json) {
    String host = json.getString("host");
    int port = json.getInteger("port", 80);
    String path = json.getString("path", "/");
    long timeout = json.getLong("timeout", 3000L);
    boolean followRedirects = json.getBoolean("followRedirects", false);
    return new RequestOptions()
        .setHost(host)
        .setPort(port)
        .setURI(path)
        .setTimeout(timeout)
        .setFollowRedirects(followRedirects);
  }


  @Override
  public void get(Handler<AsyncResult<Buffer>> completionHandler) {
    client.get(options, ar -> {
      if (ar.succeeded()) {
        HttpClientResponse response = ar.result();
        response
            .exceptionHandler(t -> completionHandler.handle(Future.failedFuture(t)))
            .bodyHandler(buffer -> completionHandler.handle(Future.succeededFuture(buffer)));
      } else {
        completionHandler.handle(Future.failedFuture(ar.cause()));
      }
    });
  }

  @Override
  public void close(Handler<Void> completionHandler) {
    this.client.close();
    completionHandler.handle(null);
  }
}
