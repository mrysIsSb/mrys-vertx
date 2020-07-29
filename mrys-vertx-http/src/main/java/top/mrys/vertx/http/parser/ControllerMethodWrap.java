package top.mrys.vertx.http.parser;

import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author mrys
 * @date 2020/7/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControllerMethodWrap {

  private Method method;
  private Class clazz;
  private Object object;
}
