package top.mrys.vertx.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author mrys
 * @date 2021/7/9
 */
public interface DataPipeline {

  DataPipeline addFirst(BaseContext context);

  DataPipeline addLast(BaseContext context);

  <H1 extends ContextHandler> BaseContext<?, H1> context(Class<H1> handlerType);
}
