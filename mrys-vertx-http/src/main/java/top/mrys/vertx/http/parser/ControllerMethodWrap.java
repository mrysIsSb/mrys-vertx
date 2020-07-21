package top.mrys.vertx.http.parser;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * @author mrys
 * @date 2020/7/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControllerMethodWrap {

    @NotNull
    private Method method;
    @NotNull
    private Class clazz;
    @NotNull
    private Object object;
}
