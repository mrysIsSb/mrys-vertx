package top.mrys.vertx.common;

import io.vertx.core.impl.ContextInternal;

/**
 * @author mrys
 * @date 2021/7/11
 */
public class SimpleDataPipeline extends AbstractDataPipeline {

  public SimpleDataPipeline(ContextInternal contextInternal,
      ChannelContext channelContext) {
    super(contextInternal, channelContext);
  }

  public static SimpleDataPipeline create(ContextInternal contextInternal,
      ChannelContext channelContext){
    return new SimpleDataPipeline(contextInternal, channelContext);
  }
}
