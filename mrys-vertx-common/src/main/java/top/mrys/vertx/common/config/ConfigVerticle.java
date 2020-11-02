package top.mrys.vertx.common.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.launcher.MyAbstractVerticle;

/**
 * @author mrys
 * @date 2020/10/29
 */
@Slf4j
public class ConfigVerticle extends MyAbstractVerticle {

  private final static String CONF_PREFIX = "-conf";

  /**
   * 配置数据
   *
   * @author mrys
   */
  private String dataKey = "config.data";


  private String[] args;


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader configLoader = context.getConfigLoader();
    vertx.eventBus().consumer(dataKey, event -> {
      JsonObject data = configLoader.getConfig();
      log.info("请求配置数据：{}", data);
      event.reply(data);
    });
    initConfig(startPromise, configLoader);
  }

  /**
   * 获取本地配置
   *
   * @author mrys
   */
  protected void initConfig(Promise<Void> startPromise, ConfigLoader configLoader) {
    ConfigRetrieverOptions options = new ConfigRetrieverOptions();
    options.setStores(getDefault());

    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.listen(event -> {
      JsonObject data = event.getNewConfiguration();
      configLoader.updateConfig(data);
    });
    retriever.getConfig(config -> {
      if (config.succeeded()) {
        updateConfig(config.result(), retriever, startPromise);
      } else {
        startPromise.fail(config.cause());
      }
    });
  }

  private void updateConfig(JsonObject json, ConfigRetriever old, Promise<Void> promise) {
    ConfigLoader configLoader = context.getConfigLoader();
    configLoader.updateConfig(json);
    List<JsonObject> centres = configLoader
        .getArrForKey(ConfigCentreStoreFactory.configCentre, JsonObject.class);
    if (CollectionUtil.isNotEmpty(centres)) {
      ConfigStoreOptions[] options = centres.stream()
          .map(jsonObject -> new ConfigStoreOptions()
              .setType(ConfigCentreStoreFactory.configCentre)
              .setOptional(true)
              .setConfig(jsonObject)
          ).toArray(ConfigStoreOptions[]::new);
      ConfigRetriever retriever1 = getConfigRetriever(options);
      retriever1.getConfig()
          .onSuccess(event -> {
            configLoader.updateConfig(event);
            configLoader.show();
          })
          .map(o -> (Void) null)
          .onComplete(promise);

      retriever1.listen(event -> {
        JsonObject newConfig = event.getNewConfiguration();
        configLoader.updateConfig(newConfig);
        configLoader.show();
      });
      old.close();
    } else {
      promise.complete();
    }
  }

  private ConfigRetriever getConfigRetriever( ConfigStoreOptions... other) {
    Vertx tempVertx = Vertx.vertx();
    ConfigRetrieverOptions op = new ConfigRetrieverOptions();
    op.addStore(getBootOptions());
    if (ArrayUtil.isNotEmpty(other)) {
      for (ConfigStoreOptions options : other) {
        op.addStore(options);
      }
    }
    op.addStore(getJsonConfig());
    return ConfigRetriever.create(tempVertx, op);
  }

  /**
   * 配置仓库 后会代替前面
   * <ol>
   *   <li>conf/boot.conf</li>
   *   <li>config()</li>
   * </ol>
   *
   * @author mrys
   */
  private List<ConfigStoreOptions> getDefault() {
    List<ConfigStoreOptions> stores = new ArrayList<>();
    stores.add(getBootOptions());
    stores.add(
        getJsonConfig());
//    stores.add(new ConfigStoreOptions().setType("sys"));
//    stores.add(new ConfigStoreOptions().setType("env"));
    return stores;
  }

  private ConfigStoreOptions getJsonConfig() {
    return new ConfigStoreOptions().setType("json")
        .setConfig(config());
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
}
