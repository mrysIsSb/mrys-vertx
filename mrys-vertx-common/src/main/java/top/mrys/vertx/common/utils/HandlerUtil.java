package top.mrys.vertx.common.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

/**
 * @author mrys
 * @date 2020/7/4
 */
@Slf4j
public class HandlerUtil {


    public static <T> Handler<AsyncResult<T>> asyncResultHandler(Consumer<T> consumer){
        return event -> {
            if (event.succeeded()) {
                consumer.accept(event.result());
            } else if (event.failed()) {
                Throwable cause = event.cause();
                log.error(cause.getMessage(), cause);
            }
        };
    }
}
