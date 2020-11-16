package top.mrys.vertx.eventbus.resolvers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.vertx.ext.web.client.HttpRequest;
import java.util.Map;
import top.mrys.vertx.common.factorys.JsonTransverterFactory;
import top.mrys.vertx.common.manager.EnumJsonTransverterNameProvider;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.other.MethodParameter;
import top.mrys.vertx.http.annotations.HeaderVar;

/**
 * @author mrys
 * @date 2020/10/19
 */
public class HeaderHttpArgumentResolver implements HttpArgumentResolver {

  private JsonTransverter jsonTransverter = JsonTransverterFactory
      .getJsonTransverter(EnumJsonTransverterNameProvider.http_server);

  /**
   * 判断参数是否满足这个解析器 实现类重写
   *
   * @param parameter
   * @author mrys
   */
  @Override
  public boolean match(MethodParameter parameter) {
    return parameter.getParameterAnnotation(HeaderVar.class) != null;
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
  public void resolver(MethodParameter parameter, Object o, HttpRequest request) {
    HeaderVar headerVar = parameter.getParameterAnnotation(HeaderVar.class);
    String name = StrUtil.isNotBlank(headerVar.value()) ? headerVar.value() : parameter.getName();

    if (o == null) {
      o = headerVar.defValue();
    }

    if (o == null) {
      if (headerVar.required()) {
        throw new NullPointerException("请求参数不能为空");
      }
    }

    if (ClassUtil.isBasicType(o.getClass()) || String.class.equals(o.getClass())) {
      request.putHeader(name, Convert.toStr(o));
    } else if (o instanceof Map) {
      ((Map) o).forEach((key, value) -> {
        if (ClassUtil.isBasicType(value.getClass())) {
          request.putHeader(Convert.toStr(key), Convert.toStr(value));
        }
      });
    } else if (BeanUtil.isBean(o.getClass())) {
      String s1 = jsonTransverter.serialize(o);
      JSONUtil.parseObj(s1)
          .forEach((s, o1) -> request.putHeader(Convert.toStr(s), Convert.toStr(o1)));
    }
  }
}
