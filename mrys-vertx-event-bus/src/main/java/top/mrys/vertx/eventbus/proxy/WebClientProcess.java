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
    HashMap<String, Object> map = new HashMap<>();
    if (ArrayUtil.isNotEmpty(args)) {
      log.info("{}",Thread.currentThread().getName());
      Arrays.stream(args).filter(s -> s.contains("="))
          .forEach(s -> {
            int index = s.indexOf("=");
            if (index == 0) {
              //开头= 往配置里查询 比如说 =es 则获取 配置里es对象赋值为map
              Map<String, Object> cmap = Vertx.currentContext().config()
                  .getJsonObject(s.substring(1)).getMap();
              map.putAll(cmap);
            } else if (index == s.length() - 1) {
              map.put(s.substring(0, s.length() - 1), null);
            } else {
              map.put(s.substring(0, index + 1), s.substring(index, s.length()));
            }
          });
    }
    return client.request(method, Convert.toInt(map.get("port"), 80),
        Convert.toStr(map.get("host"), "localhost"), path);
  }
}
