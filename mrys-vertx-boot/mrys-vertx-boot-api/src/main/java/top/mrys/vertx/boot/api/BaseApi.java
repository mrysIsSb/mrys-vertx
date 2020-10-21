package top.mrys.vertx.boot.api;

import io.vertx.core.Future;
import top.mrys.vertx.http.annotations.ReqParam;

/**
 * @author mrys
 * @date 2020/10/21
 */
public interface BaseApi<T> {

  Future<T> testBase(@ReqParam Integer id);

}
