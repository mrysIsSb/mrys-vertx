package top.mrys.vertx.http.starter;

import cn.hutool.core.util.StrUtil;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.impl.HttpServerConnection;
import io.vertx.core.impl.VertxInternal;
import io.vertx.ext.web.Router;
import java.util.Set;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.consts.ConstLog;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;
import top.mrys.vertx.http.parser.RouteFactory;

/**
 * @author mrys
 * @date 2020/9/12
 */
@Slf4j
public class HttpVerticle extends MyAbstractVerticle {

  @Getter
  @Setter
  private Supplier<Integer> port;
  @Getter
  @Setter
  private Supplier<Set<Class>> routeClassProvider;

  private static ChannelGroup cg;


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    cg = new DefaultChannelGroup(
        ((VertxInternal) vertx).getAcceptorEventLoopGroup().next());
    RouteFactory routeFactory = context.getInstance(RouteFactory.class);
    routeFactory.addObjectInstanceFactory(context.getInstanceFactory());
    routeFactory.addClasses(routeClassProvider.get());
    routeFactory.addVertx(vertx);
    Router router = routeFactory.get();
    router.get("/client/list").handler(event -> event.end(StrUtil.toString(HttpVerticle.cg.size())));

    HttpServerOptions options = new HttpServerOptions();
    options.setLogActivity(true);
    vertx.createHttpServer(options)
        .connectionHandler(event -> {
          if (event instanceof HttpServerConnection) {
            HttpServerConnection connection = (HttpServerConnection) event;
            HttpVerticle.cg.add(connection.channel());
          }
        })
        .requestHandler(router)
        .listen(port.get())
        .onSuccess(event -> startPromise.complete())
        .onFailure(startPromise::fail);
  }

  @Override
  public void stop() throws Exception {
    log.info(ConstLog.log_template1, "http stop");
  }
}
