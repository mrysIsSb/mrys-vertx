package top.mrys.vertx.http.parser;

import cn.hutool.core.collection.CollectionUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.Arrays;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.common.utils.Interceptor;

/**
 * @author mrys
 * @date 2020/8/4
 */
@Slf4j
@Component
public class HttpInterceptor implements Interceptor<RoutingContext, Object> {

  @Override
  public boolean preHandler(RoutingContext routingContext) {
    String path = routingContext.request().path();
    log.debug("-->{}", path);
    if (path.matches("/err\\S*")) {
      routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8")
          .end("err");
    } else if (path.matches("/fail\\S*")) {
    } else if (path.matches("/restart\\S*")) {
      MyLauncher.context.refresh();
      routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8")
          .end("restart");
    } else if (path.matches("/did\\S*")) {
      Set<String> set = routingContext.vertx().deploymentIDs();
      routingContext.response()
          .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8")
          .end(Arrays.toString(set.toArray()));
    } else {
      return true;
    }
    return false;
  }
}
