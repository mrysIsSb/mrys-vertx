package top.mrys.vertx.eventbus.proxy;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.config.ConfigLoader;
import top.mrys.vertx.http.RouteUtil;

/**
 * @author mrys
 * @date 2020/11/11
 */
@Slf4j
public class WebClientProcess {


  /**
   * 处理webclient
   *
   * @author mrys
   */
  public HttpRequest<Buffer> process(WebClient client, HttpMethod method, String path,
      String[] args) {
    Map<String, Object> map = RouteUtil.getArgs(args);
    return client.request(method, Convert.toInt(map.get("port"), 80),
        Convert.toStr(map.get("host"), "localhost"), path);
  }
}
