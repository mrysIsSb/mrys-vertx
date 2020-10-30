package top.mrys.vertx.common.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mrys
 * @date 2020/10/29
 */
@Slf4j
public class ConfigVerticle extends AbstractVerticle {

  private final static String CONF_PREFIX = "-conf";

  private String[] args;

  private JsonObject data = new JsonObject();


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.eventBus().consumer("config.data", event -> {
      log.info("请求配置数据：{}", data);
      event.reply(data);
    });
    ConfigRetriever retriever = ConfigRetriever.create(vertx);
    retriever.getConfig(config -> {
      log.info(config.result().toString());
      data = config.result();
    });
    retriever.listen(event -> {
      data = event.getNewConfiguration();
      log.info("更新配置数据:{}", data);
      vertx.eventBus().publish("config.data.update", data);
    });
    startPromise.complete();
  }

  private void updateConfig(JsonObject json, Promise<Void> promise) {
    ConfigRepo configRepo = ConfigRepo.getInstance();
    configRepo.mergeInData(json);
    List<JsonObject> centres = configRepo.getArrForKey("configCentre", JsonObject.class);
    if (CollectionUtil.isNotEmpty(centres)) {
      ConfigStoreOptions[] options = centres.stream()
          .map(jsonObject -> new ConfigStoreOptions()
              .setType(ConfigCentreStoreFactory.configCentre)
              .setOptional(true)
              .setConfig(jsonObject)
          ).toArray(ConfigStoreOptions[]::new);
      ConfigRetriever retriever1 = getConfigRetriever(args, options);
      retriever1.getConfig()
          .onSuccess(event -> configRepo.mergeInData(event).resolve())
          .map(o -> (Void) null)
          .onComplete(promise);

      retriever1.listen(event -> {
        JsonObject json1 = event.getNewConfiguration();
        configRepo.mergeInData(json1).resolve();
      });
    }
  }

  private static ConfigRetriever getConfigRetriever(String[] args, ConfigStoreOptions... other) {
    Vertx tempVertx = Vertx.vertx();
    ConfigRetrieverOptions op = new ConfigRetrieverOptions();
    if (ArrayUtil.isNotEmpty(other)) {
      for (ConfigStoreOptions options : other) {
        op.addStore(options);
      }
    }
    op.addStore(getBootOptions()).addStore(getArgsConfigStoreOptions(args));
    return ConfigRetriever.create(tempVertx, op);
  }

  private static ConfigStoreOptions getBootOptions() {
    return new ConfigStoreOptions()
        .setType("file")
        .setFormat("hocon")
        .setOptional(true)
        .setConfig(new JsonObject()
            .put("path", "conf" + File.separator + "boot.conf")
        );
  }

  /**
   * 获取传递过来的参数
   *
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
