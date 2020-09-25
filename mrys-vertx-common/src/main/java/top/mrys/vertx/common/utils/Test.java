package top.mrys.vertx.common.utils;

import io.vertx.core.Future;

/**
 * @author mrys
 * @date 2020/9/23
 */
public interface Test {

  Future<String> testMethod(String id);

}
