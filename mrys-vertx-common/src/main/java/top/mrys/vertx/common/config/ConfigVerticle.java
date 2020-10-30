package top.mrys.vertx.common.config;

import cn.hutool.json.JSONUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import java.io.File;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mrys
 * @date 2020/10/29
 */
@Slf4j
public class ConfigVerticle extends AbstractVerticle {

  private final static String CONF_PREFIX = "-conf";

  private String[] args;



  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigRetriever retriever = ConfigRetriever.create(vertx);
    retriever.getConfig(config->{
      log.info(config.result().toString());
      vertx.eventBus().publish("config.data", config.result());
    });
    startPromise.complete();
  }

  private static ConfigStoreOptions getBootOptions() {
    return new ConfigStoreOptions()
        .setType("file")
        .setFormat("hocon")
        .setOptional(true)
        .setConfig(new JsonObject()
            .put("path", "conf" + File.separator +"boot.conf")
        );
  }

  /**
   * 获取传递过来的参数
   * @author mrys
   */
  private static ConfigStoreOptions getArgsConfigStoreOptions(String[] args) {
    JsonObject json = new JsonObject();
    if (args != null && args.length > 0) {
      for (String arg : args) {
        if (arg.startsWith(CONF_PREFIX)) {
          String conf = arg.substring(CONF_PREFIX.length() + 1);
          if (JSONUtil.isJson(conf)) {
            json = new JsonObject(conf);
          }
        }
      }
    }
    return new ConfigStoreOptions()
        .setType("json")
        .setOptional(true)
        .setConfig(json);
  }
}
