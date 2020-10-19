package top.mrys.vertx.eventbus;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.vertx.ext.web.client.HttpRequest;
import java.util.Map;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.PathVar;
import top.mrys.vertx.http.annotations.ReqParam;

/**
 * @author mrys
 * @date 2020/10/19
 */
public class ParamHttpArgumentResolver implements HttpArgumentResolver {

  /**
   * 判断参数是否满足这个解析器 实现类重写
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    return parameter.getParameterAnnotation(ReqParam.class) != null;
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
    ReqParam reqParam = parameter.getParameterAnnotation(ReqParam.class);
    String name = StrUtil.isNotBlank(reqParam.value()) ? reqParam.value() : parameter.getName();
    if (reqParam.required() && o == null) {
      throw new NullPointerException("请求参数不能为空");
    }
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
}
