package top.mrys.vertx.http.interfaces.impls;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import top.mrys.vertx.http.RouteUtil;
import top.mrys.vertx.http.interfaces.HeaderValueProcess;

/**
 * @author mrys
 * @date 2020/12/7
 */
public class EsAuthorizationHeaderProcess implements HeaderValueProcess {

  @Override
  public Map<String, String> process(String key, String[] args) {
    Map<String, Object> map = RouteUtil.getArgs(args);
    Object username = map.get("username");
    Object password = map.get("password");
    if (username != null && password != null) {
      String encode = Base64.encode(Convert.toStr(username) + ":" + Convert.toStr(password));
      HashMap<String, String> result = new HashMap<>();
      result.put("Authorization", "Basic " + encode);
      return result;
    }
    return Collections.emptyMap();
  }
}
