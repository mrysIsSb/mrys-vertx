package top.mrys.vertx.common.utils;

/**
 * 拦截器
 * @author mrys
 * @date 2020/8/3
 */
public interface Interceptor<PRE,POST> {

  default boolean preHandler(PRE pre) {
    return true;
  }

  default void postHandle(POST post) {

  }

}
