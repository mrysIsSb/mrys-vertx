package top.mrys.vertx.http.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
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
    vertx.createHttpServer().requestHandler(router)
        .listen(port)
        .onSuccess(event -> startPromise.complete())
        .onFailure(startPromise::fail);
  }


  @Override
  public void stop() throws Exception {
    log.info(ConstLog.log_template1, "http stop");
  }
}
