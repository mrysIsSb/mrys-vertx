package top.mrys.core;

/**
 * @author mrys
 * 2021/5/28
 */
@FunctionalInterface
public interface F<X, Y> {

  Y apply(X x);
}
