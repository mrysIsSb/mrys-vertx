package top.mrys.vertx.common.utils;


import cn.hutool.core.lang.func.Func;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.ContextInternal;
import java.util.Objects;
import java.util.function.Predicate;
import lombok.Getter;

/**
 * @author mrys
 * @date 2020/10/19
 */
public class FutureUtil<T> {

  @Getter
  private Future<T> future;

  public FutureUtil(Future<T> future) {
    this.future = future;
  }

  public FutureUtil<T> predicateAndFailedRecover(Predicate<T> test, Future<T> mapper) {
    ContextInternal ctx = (ContextInternal) future.context();
    Promise<T> ret;
    if (ctx != null) {
      ret = ctx.promise();
    } else {
      ret = Promise.promise();
    }
    future.onComplete(r -> {
      //没成功或满足条件
      if (!r.succeeded() || test.test(r.result())) {
        mapper.onComplete(ret);
      }else {
        ret.complete(r.result());
      }
    });
    future = ret.future();
    return this;
  }

  public FutureUtil<T> nullOrFailedRecover(Future<T> mapper) {
    return predicateAndFailedRecover(Objects::isNull, mapper);
  }

}
