package top.mrys.vertx.http.parser;

/**
 * 拦截器
 * @author mrys
 * @date 2020/8/3
 */
public interface Interceptor<T> {

  boolean preHandler(T t);
}
