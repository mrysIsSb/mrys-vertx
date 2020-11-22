package top.mrys.vertx.boot.service;

import io.vertx.core.Future;
import top.mrys.vertx.boot.api.BaseApi;
import top.mrys.vertx.springboot.http.server.annotations.GetRoute;

/**
 * @author mrys
 * @date 2020/10/21
 */
public class BaseServiceImpl<T> implements BaseApi<T> {

  @GetRoute
  public Future<T> testBase(Integer id) {
    return Future.succeededFuture();
  }

}
