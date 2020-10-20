package top.mrys.vertx.eventbus;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.vertx.ext.web.client.HttpRequest;
import java.util.Map;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.ReqParam;

/**
 * @author mrys
 * @date 2020/10/20
 */
public class DefaultHttpArgumentResolver implements HttpArgumentResolver {

  /**
   * 判断参数是否满足这个解析器 实现类重写
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    return true;
  }

  /**
   * 将参数解析到http请求
   *
   * @param parameter
   * @param o
   * @param request
   * @author mrys
   */
  @Override
  public <T> void resolver(MethodParameter parameter, T o, HttpRequest request) {
    String name = parameter.getName();
    if (ClassUtil.isBasicType(o.getClass())) {
      request.addQueryParam(name, Convert.toStr(o));
    } else if (o instanceof Map) {
      ((Map) o).forEach((key, value) -> {
        if (ClassUtil.isBasicType(value.getClass())) {
          request.addQueryParam(Convert.toStr(key), Convert.toStr(value));
        }
      });
    } else if (BeanUtil.isBean(o.getClass())) {
      JSONUtil.parseObj(o)
          .forEach((s, o1) -> request.addQueryParam(Convert.toStr(s), Convert.toStr(o1)));
    }
  }

  /**
   * 越大越优先
   *
   * @author mrys
   */
  @Override
  public int order() {
    return Integer.MIN_VALUE;
  }
}
