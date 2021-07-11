package top.mrys.vertx.common;

import io.vertx.core.impl.ContextInternal;

/**
 * @author mrys
 * @date 2021/7/11
 */
public abstract class AbstractChannelContext implements ChannelContext {

  private final DataPipeline pipe;
  private final ContextInternal contextInternal;

  public AbstractChannelContext(ContextInternal contextInternal) {
    this.contextInternal = contextInternal;
    this.pipe = SimpleDataPipeline.create(contextInternal, this);
  }

  @Override
  public DataPipeline getPipe() {
    return pipe;
  }

  @Override
  public ContextInternal getContext() {
    return contextInternal;
  }
}
