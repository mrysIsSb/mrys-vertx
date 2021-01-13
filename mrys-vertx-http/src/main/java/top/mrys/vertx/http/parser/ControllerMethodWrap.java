package top.mrys.vertx.http.parser;

import java.lang.reflect.Method;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.mrys.vertx.common.other.MethodParameter;

/**
 * @author mrys
 * @date 2020/7/9
 */
@Data
@NoArgsConstructor
public class ControllerMethodWrap {

  /**
   * 方法
   *
   * @author mrys
   */
  private Method method;
  /**
   * controller class
   *
   * @author mrys
   */
  private Class clazz;

  /**
   * controller 对象 用在spring 或原生的时候的invoke
   *
   * @author mrys
   */
  private Object object;

  /**
   * 方法参数
   *
   * @author mrys
   */
  private MethodParameter[] methodParameters;


  private ControllerMethodWrap(Method method, Class clazz, Object object) {
    this.method = method;
    this.clazz = clazz;
    this.object = object;
    this.methodParameters = MethodParameter.create(method);
  }

  public static ControllerMethodWrap create(Method method, Class clazz, Object object) {
    return new ControllerMethodWrap(method, clazz, object);
  }

}
