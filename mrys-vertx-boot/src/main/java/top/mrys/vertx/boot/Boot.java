package top.mrys.vertx.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.http.starter.EnableHttp;
import top.mrys.vertx.mysql.starter.EnableMysql;

/**
 *     /*run.createHttpServer().requestHandler(event -> {
 *       if (event instanceof Http1xServerRequest) {
 *         Http1xServerRequest request = (Http1xServerRequest) event;
 *         HttpConnection connection = request.connection();
 *         if (connection instanceof Http1xServerConnection) {
 *           Http1xServerConnection connection1 = (Http1xServerConnection) connection;
 *           Channel channel = connection1.channel();
 *           List<String> names = channel.pipeline().names();
 *           if (!names.contains("beforeHandler")) {
 *             channel.pipeline().addBefore("handler","beforeHandler",new ChannelInboundHandlerAdapter(){
 *
 *               @Override
 *               public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
 *                 System.out.println("---1111111111111111111111111111111");
 *                 if (msg instanceof DefaultHttpRequest) {
 *                   DefaultHttpRequest r = (DefaultHttpRequest) msg;
 *                 }
 *                 System.out.println(msg);
 *                 super.channelRead(ctx, msg);
 *               }
 *             });
 *           }
 *           if (!names.contains("afterHandler")) {
 *             channel.pipeline().addBefore("handler","afterHandler",new ChannelOutboundHandlerAdapter(){
 *
 *               @Override
 *               public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
 *                   throws Exception {
 *                 System.out.println("22222222222222222222222");
 *                 FullHttpResponse msg1 = (FullHttpResponse) msg;
 *                 System.out.println(msg1.content().toString(StandardCharsets.UTF_8));
 *                 super.write(ctx, msg, promise);
 *               }
 *
 *               @Override
 *               public void flush(ChannelHandlerContext ctx) throws Exception {
 *                 System.out.println("333333333333333333333333333333333");
 *                 super.flush(ctx);
 *               }
 *             });
 *           }
 *           ChannelHandlerContext context = connection1.channelHandlerContext();
 *           context.pipeline().remove();
 *         }
 *       }
 *       System.out.println("-------------------------------"+event.absoluteURI());
 *       event.response().end("hello");
 *     }).listen(8080);
 *    CompositeFuture.all().
 * @author mrys
 * @date 2020/7/21
 */
@EnableHttp(port = 8888,scanPackage = "top.mrys.vertx.boot.controller")
@EnableMysql
@Slf4j
@ComponentScan
public class Boot {

  public static void main(String[] args) {
    System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
    for (String name : MyLauncher.run(Boot.class, args).getBeanDefinitionNames()) {
      System.out.println(name);
    }

  }
}
