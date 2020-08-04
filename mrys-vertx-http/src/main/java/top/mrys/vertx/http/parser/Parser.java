package top.mrys.vertx.http.parser;

import java.util.function.BiConsumer;

/**
 * @author mrys
 * @date 2020/7/9
 */
public interface Parser<T, U> extends BiConsumer<T, U> {

    /**
     * 是否执行
     *
     * @author mrys
     */
    default Boolean canExec(T object) {
        return true;
    }

}
