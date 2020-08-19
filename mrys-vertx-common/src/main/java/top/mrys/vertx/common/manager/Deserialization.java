package top.mrys.vertx.common.manager;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 反序列化
 *
 * @author mrys
 * @date 2020/8/19
 */
public interface Deserialization<T> {

  <R> R deSerialize(T o, Class<R> rClass);
}
