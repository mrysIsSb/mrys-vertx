package top.mrys.vertx.http.parser;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.RoutingContext;
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
public class HttpInterceptor implements Interceptor<RoutingContext,Object> {

  @Override
  public boolean preHandler(RoutingContext routingContext) {
    String path = routingContext.request().path();
    log.info("-->{}",path);
    if (path.matches("/err\\S*")){
      routingContext.response().putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON+";charset=utf-8").end("err");
    }else if (path.matches("/fail\\S*")){
      return false;
    }else if (path.matches("/restart\\S*")){
      MyLauncher.context.refresh();
    }
    return true;
  }
}
