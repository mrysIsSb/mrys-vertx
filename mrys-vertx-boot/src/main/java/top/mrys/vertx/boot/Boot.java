package top.mrys.vertx.boot;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpConnection;
import io.vertx.core.http.impl.Http1xServerConnection;
import io.vertx.core.http.impl.Http1xServerRequest;
import io.vertx.core.impl.ContextInternal;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import sun.reflect.misc.FieldUtil;
import top.mrys.vertx.common.launcher.MyLauncher;

/**
 * @author mrys
 * @date 2020/7/21
 */
public class Boot {

  public static void main(String[] args) {
    Vertx run = MyLauncher.run(Boot.class, args);
    run.createHttpServer().requestHandler(event -> {
      if (event instanceof Http1xServerRequest) {
        Http1xServerRequest request = (Http1xServerRequest) event;
        HttpConnection connection = request.connection();
        if (connection instanceof Http1xServerConnection) {
          Http1xServerConnection connection1 = (Http1xServerConnection) connection;
          Channel channel = connection1.channel();
          List<String> names = channel.pipeline().names();
          if (!names.contains("beforeHandler")) {
            channel.pipeline().addBefore("handler","beforeHandler",new ChannelInboundHandlerAdapter(){

              @Override
              public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                System.out.println("---1111111111111111111111111111111");
                if (msg instanceof DefaultHttpRequest) {
                  DefaultHttpRequest r = (DefaultHttpRequest) msg;
                }
                System.out.println(msg);
                super.channelRead(ctx, msg);
              }
            });
          }
          if (!names.contains("afterHandler")) {
            channel.pipeline().addBefore("handler","afterHandler",new ChannelOutboundHandlerAdapter(){

              @Override
              public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
                  throws Exception {
                System.out.println("22222222222222222222222");
                FullHttpResponse msg1 = (FullHttpResponse) msg;
                System.out.println(msg1.content().toString(StandardCharsets.UTF_8));
                super.write(ctx, msg, promise);
              }

              @Override
              public void flush(ChannelHandlerContext ctx) throws Exception {
                System.out.println("333333333333333333333333333333333");
                super.flush(ctx);
              }
            });
          }
          /*ChannelHandlerContext context = connection1.channelHandlerContext();
          context.pipeline().remove();*/
        }
      }
      System.out.println("-------------------------------"+event.absoluteURI());
      event.response().end("hello");
    }).listen(8080);
//    CompositeFuture.all().
  }
}
