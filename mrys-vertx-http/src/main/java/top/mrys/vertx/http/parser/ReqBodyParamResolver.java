package top.mrys.vertx.http.parser;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import java.nio.charset.StandardCharsets;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.common.manager.JsonTransverter;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.http.annotations.ReqBody;

/**
 * @author mrys
 * @date 2020/9/22
 */
public class ReqBodyParamResolver implements ParamResolver {

  @Override
  public boolean match0(HttpParamType type) {
    return EnumParamFrom.BODY.equals(type.getFrom())
        && AnnotationUtil.isHaveAnyAnnotations(type.getAnnotation(), ReqBody.class);
  }

  @Override
  public <T> Future<T> resolve(HttpParamType<T> type, RoutingContext context) {
//    ReqBody reqBody = AnnotationUtil.getAnnotation(type.getAnnotation(), ReqBody.class);
    return context.request().body().map(buffer -> {
      JsonTransverter bean = MyLauncher.context.getBean(JsonTransverter.class);
      return bean.deSerialize(buffer.toString(), type.getClazz());
    });
  }

}
