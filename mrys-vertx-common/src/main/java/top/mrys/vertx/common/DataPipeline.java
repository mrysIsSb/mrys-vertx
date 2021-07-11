package top.mrys.vertx.common;

/**
 * @author mrys
 * @date 2021/7/9
 */
public interface DataPipeline extends HandleInvoker{

  DataPipeline addFirst(ContextHandler handler);

  DataPipeline addLast(ContextHandler handler);

  DataPipeline remove(ContextHandler handler);

  <H1 extends ContextHandler> ProcessContext<?, H1, ?> context(Class<H1> handlerType);
}
