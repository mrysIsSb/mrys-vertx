package top.mrys.vertx.common.utils;


import io.vertx.core.Closeable;
import io.vertx.core.Context;
import io.vertx.core.impl.EventLoopContext;
import io.vertx.core.impl.WorkerContext;

/**
 * @author mrys
 * 2021/5/27
 */
public class ContextUtil {

  public static void addCloseHook(Context context, Closeable closeable) {
    if (context instanceof EventLoopContext) {
      ((EventLoopContext) context).addCloseHook(closeable);
    } else if (context instanceof WorkerContext) {
      ((WorkerContext) context).addCloseHook(closeable);
    }
  }
}
