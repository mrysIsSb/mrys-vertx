package top.mrys.vertx.eventbus.proxy;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import java.util.HashMap;

/**
 * @author mrys
 * @date 2020/11/11
 */
public class WebClientProcess {


  public HttpRequest<Buffer> process(WebClient client, HttpMethod method, String path,
      String[] args) {
    HashMap<String, String> map = new HashMap<>();
    if (ArrayUtil.isNotEmpty(args)) {
      for (String arg : args) {
        String[] split = arg.split("=");
        if (split.length == 2) {
          map.put(split[0], split[1]);
        }else {
          map.put(arg, null);
        }
      }
    }
    return client.request(method, Convert.toInt(map.get("port"), 8080),
        Convert.toStr(map.get("host"), "localhost"), path);
  }
}
