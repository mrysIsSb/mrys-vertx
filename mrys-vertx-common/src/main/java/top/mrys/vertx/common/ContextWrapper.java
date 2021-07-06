package top.mrys.vertx.common;

import io.vertx.core.impl.ContextInternal;

/**
 * @author mrys
 * @date 2021/7/6
 */
public interface ContextWrapper {

  /**
   * 获取上下文
   *
   * @author mrys
   */
  ContextInternal getContext();

}
