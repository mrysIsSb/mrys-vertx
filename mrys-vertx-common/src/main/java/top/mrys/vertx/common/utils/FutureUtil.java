package top.mrys.vertx.common.utils;


import java.util.Objects;
import java.util.function.Predicate;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.future.FailedFuture;
import io.vertx.core.impl.future.FutureInternal;
import io.vertx.core.impl.future.PromiseInternal;
import lombok.Getter;

/**
 * @author mrys
 * @date 2020/10/19
 */
public class FutureUtil<T> {

  @Getter
  private FutureInternal<T> future;

  public FutureUtil(FutureInternal<T> future) {
    this.future = future;
  }

  public FutureUtil<T> predicateAndFailedRecover(Predicate<T> test, Future<T> mapper) {
    ContextInternal ctx = future.context();
    PromiseInternal<T> ret;
    if (ctx != null) {
      ret = ctx.promise();
    } else {
      ret = (PromiseInternal<T>) Promise.promise();
    }
    future.onComplete(r -> {
      //没成功或满足条件
      if (!r.succeeded() || test.test(r.result())) {
        mapper.onComplete(ret);
      } else {
        ret.complete(r.result());
      }
    });
    future = (FutureInternal<T>) ret.future();
    return this;
  }

  public FutureUtil<T> nullOrFailedRecover(Future<T> mapper) {
    return predicateAndFailedRecover(Objects::isNull, mapper);
  }

  /**
   * 创建一个初始化的future
   *
   * @author mrys
   */
  public static <T> FutureUtil<T> createInitFuture(String msg) {
    ContextInternal context = (ContextInternal) Vertx.currentContext();
    return new FutureUtil<>(new FailedFuture<>(context, msg));
  }

}
