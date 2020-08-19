package top.mrys.vertx.common.manager;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * 序列化
 *
 * @author mrys
 * @date 2020/8/19
 */
public interface Serialization<T> {

  T serialize(Object o);

}
