package top.mrys.vertx.common.launcher;

import java.lang.annotation.Annotation;

/**
 * @author mrys
 * @date 2020/8/4
 */
public interface Starter<A extends Annotation> {

  void start(A a);
}
