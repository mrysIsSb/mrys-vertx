package top.mrys.vertx.http.interfaces;

import java.util.Map;

/**
 * @author mrys
 * @date 2020/12/7
 */
public interface HeaderValueProcess {

  Map<String, String> process(String key, String[] args);

}
