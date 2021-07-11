package top.mrys.vertx.common;

import io.vertx.core.Closeable;

/**
 * @author mrys
 * @date 2021/7/11
 */
public interface ChannelContext extends ContextWrapper, Closeable {

  DataPipeline getPipe();

}
