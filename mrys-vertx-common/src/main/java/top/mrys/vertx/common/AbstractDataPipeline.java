package top.mrys.vertx.common;

import io.vertx.core.impl.ContextInternal;
import top.mrys.vertx.common.utils.BaseContextUtil;

/**
 * @author mrys
 * @date 2021/7/10
 */
public abstract class AbstractDataPipeline implements DataPipeline {

  private AbstractProcessContext head;
  private AbstractProcessContext tail;

  private final ContextInternal contextInternal;
  private final ChannelContext channelContext;

  public AbstractDataPipeline(ContextInternal contextInternal,
      ChannelContext channelContext) {
    this.contextInternal = contextInternal;
    this.channelContext = channelContext;
    head = SimpleProcessContext.create(contextInternal, new AbstractContextHandler() {
    }, channelContext);
    tail = SimpleProcessContext.create(contextInternal, new AbstractContextHandler() {
    }, channelContext);
    head.next = tail;
    tail.prev = head;
  }

  @Override
  public DataPipeline addFirst(ContextHandler handler) {
    SimpleProcessContext<ContextHandler, ChannelContext> con = newContext(handler);
    addNext(head, con);
    con.handler().handlerAdded(con);
    return this;
  }


  @Override
  public DataPipeline addLast(ContextHandler handler) {
    SimpleProcessContext<ContextHandler, ChannelContext> con = newContext(handler);
    addPrev(tail, con);
    con.handler().handlerAdded(con);
    return this;
  }

  @Override
  public DataPipeline remove(ContextHandler handler) {
    synchronized (this) {
      AbstractProcessContext context = context(handler);
      if (context != null) {
        context.prev.next = context.next;
        context.next.prev = context.prev;
        context.handler().handlerRemoved(context);
      }
      return this;
    }
  }

  @Override
  public final <H1 extends ContextHandler> ProcessContext<?, H1, ?> context(Class<H1> handlerType) {
    return BaseContextUtil.getNextBaseContextByType(head, handlerType);
  }

  @Override
  public void fireHandle(Object data) {
    head.fireHandle(data);
  }

  @Override
  public void fireExceptionCaught(Throwable cause) {
    head.fireExceptionCaught(cause);
  }

  private SimpleProcessContext<ContextHandler, ChannelContext> newContext(ContextHandler handler) {
    return SimpleProcessContext
        .create(contextInternal, handler, channelContext);
  }

  private void addNext(AbstractProcessContext current, AbstractProcessContext next) {
    synchronized (this) {
      next.prev = current;
      next.next = current.next;
      current.next.prev = next;
      current.next = next;
    }
  }

  private void addPrev(AbstractProcessContext current, AbstractProcessContext prev) {
    synchronized (this) {
      prev.prev = current.prev;
      prev.next = current;
      current.prev.next = prev;
      current.prev = prev;
    }
  }

  private AbstractProcessContext context(ContextHandler handler) {
    AbstractProcessContext context = head.next;
    for (; ; ) {
      if (context == null) {
        return null;
      }
      if (context.handler() == handler) {
        return context;
      }
      context = context.next;
    }
  }
}
