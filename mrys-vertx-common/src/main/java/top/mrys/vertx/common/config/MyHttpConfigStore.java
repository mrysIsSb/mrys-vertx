//package top.mrys.vertx.common.config;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import io.vertx.config.spi.ConfigStore;
//import io.vertx.core.AsyncResult;
//import io.vertx.core.CompositeFuture;
//import io.vertx.core.Context;
//import io.vertx.core.Future;
//import io.vertx.core.Handler;
//import io.vertx.core.Promise;
//import io.vertx.core.Vertx;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.http.HttpClient;
//import io.vertx.core.http.HttpClientOptions;
//import io.vertx.core.http.HttpClientRequest;
//import io.vertx.core.http.HttpClientResponse;
//import io.vertx.core.http.HttpMethod;
//import io.vertx.core.http.RequestOptions;
//import io.vertx.core.json.JsonObject;
//
///**
// * @author mrys
// * @date 2020/9/18
// */
//public class MyHttpConfigStore implements ConfigStore {
//
//  private final HttpClient client;
//
//  private final String[] keys;
//  private final JsonObject config;
//
//  private boolean closed;
//  private final Context ctx;
//
//  public MyHttpConfigStore(Vertx vertx, JsonObject json) {
//    this.ctx = vertx.getOrCreateContext();
//    this.client = vertx.createHttpClient(new HttpClientOptions(json));
//    this.config = json;
//    this.keys = (String[]) json.getJsonArray("keys").getList().stream().toArray(String[]::new);
//  }
//
//  private RequestOptions createRequestOptions(JsonObject json, String path) {
//    String host = json.getString("host");
//    int port = json.getInteger("port", 80);
//    long timeout = json.getLong("timeout", 3000L);
//    boolean followRedirects = json.getBoolean("followRedirects", false);
//    HttpMethod method = HttpMethod.valueOf(json.getString("method", "GET"));
//    return new RequestOptions()
//            .setMethod(method)
//            .setHost(host)
//            .setPort(port)
//            .setURI(path)
//            .setTimeout(timeout)
//            .setFollowRedirects(followRedirects);
//  }
//
//
//  public void close(Handler<Void> completionHandler) {
//    if (Vertx.currentContext() == this.ctx) {
//      if (!this.closed) {
//        this.closed = true;
//        this.client.close();
//      }
//    } else {
//      this.ctx.runOnContext((v) -> this.close(completionHandler));
//    }
//
//  }
//
//  @Override
//  public void get(Handler<AsyncResult<Buffer>> completionHandler) {
//    List<Future> list = new ArrayList<>();
//    for (String key : keys) {
//      Promise<Buffer> promise = Promise.promise();
//      RequestOptions options = createRequestOptions(config, key);
//      HttpClientRequest request = client.request(options)
//      request.exceptionHandler(t -> completionHandler.handle(Future.failedFuture(t)))
//              .onComplete(event -> {
//                if (event.succeeded()) {
//                  HttpClientResponse result = event.result();
//                  result.exceptionHandler(t -> completionHandler.handle(Future.failedFuture(t)));
//                  Future<Buffer> body = result.body();
//                  body.onComplete(buffer -> {
//                    if (buffer.succeeded()) {
//                      promise.complete(buffer.result());
//                    } else {
//                      promise.fail(buffer.cause());
//                    }
//                  });
//                } else {
//                  promise.fail(event.cause());
//                }
//              })
//              .end();
//      list.add(promise.future());
//    }
//    CompositeFuture.all(list)
//            .onComplete(event -> {
//              if (event.succeeded()) {
//                CompositeFuture result = event.result();
//                List<Buffer> list1 = result.list();
//                Buffer buffer = list1.stream().filter(Objects::nonNull).map(Buffer::toJsonObject)
//                        .reduce((json1, json2) -> json1.mergeIn(json2, true)).get().toBuffer();
//                completionHandler.handle(Future.succeededFuture(buffer));
//              } else {
//                completionHandler.handle(Future.failedFuture(event.cause()));
//              }
//            });
//  }
//
//}
