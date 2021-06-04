package top.mrys.vertx.config.centre;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.config.common.StringConstant;

/**
 * @author mrys
 * 2021/5/26
 */
@Slf4j
public class ConfigCentreVerticle extends AbstractVerticle {
  private ConfigRetrieverOptions store;

  private JsonObject config;


  public ConfigCentreVerticle(ConfigRetrieverOptions store) {
    this.store = store;
  }

  @Override
  public void start(Promise<Void> promise) throws Exception {
    config = config();

    vertx.eventBus().<String>consumer(StringConstant.request_event_bus_address, msg -> {
      String key = msg.body();
      msg.reply(this.config.getJsonObject(key));
    });

    JsonObject configCentre = config.getJsonObject("configCentre");
    if (configCentre != null) {
      getFromOtherConfig(configCentre.getJsonArray("stores"));
    }

    ConfigRetriever retriever = ConfigRetriever.create(vertx, store);
    retriever.listen(configChange -> {
      log.info("更新配置{}", configChange.getNewConfiguration());
      vertx.eventBus().publish(StringConstant.config_update, "");
      this.config.mergeIn(configChange.getNewConfiguration(), true);
    });

    retriever.getConfig()
            .onSuccess(json -> this.config.mergeIn(json, true))
            .onSuccess(json -> log.info("获取配置{}", json.toString()))
            .<Void>mapEmpty()
            .onComplete(promise);

  }

  private void getFromOtherConfig(JsonArray stores) {
    if (stores != null && !stores.isEmpty()) {
      for (Object o : stores) {
        if (o instanceof JsonObject) {
          JsonObject s = (JsonObject) o;
          Boolean enable = s.getBoolean("enable", false);
          if (enable) {
            store.addStore(new ConfigStoreOptions(s));
          }
        }

      }
    }
  }
}
