package top.mrys.vertx.config.common;


import cn.hutool.core.util.StrUtil;
import io.vertx.config.ConfigChange;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author mrys
 * 2021/4/17
 */
public abstract class CustomVerticle extends AbstractVerticle {

  protected JsonObject config = new JsonObject();

  private ConfigRetriever retriever;

  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    config.mergeIn(context.config(), true);
  }

  protected String getServerName() {
    return null;
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    String serverName = config.getString("serverName", getServerName());
    if (StrUtil.isNotBlank(serverName)) {
      config.put("serverName", serverName);
      ConfigRetrieverOptions store = new ConfigRetrieverOptions()
              .addStore(new ConfigStoreOptions()
                      .setType(StringConstant.event_bus_type)
                      .setConfig(new JsonObject()
                              .put("address", serverName)
                      ))
              .setScanPeriod(2000);
      retriever = ConfigRetriever.create(vertx, store);
      retriever.getConfig().onSuccess(event -> {
        config.mergeIn(event);
        start0(startPromise);
      });
      retriever.listen(event -> config.mergeIn(event.getNewConfiguration()));
      //可以去了
      ContextUtil.addCloseHook(context, completion -> {
        retriever.close();
        completion.complete();
      });
    } else {
      start0(startPromise);
    }
  }

  protected abstract void start0(Promise<Void> startPromise);

  @Override
  public JsonObject config() {
    return config;
  }


  protected void addConfigUpdateHandler(Handler<ConfigChange> handler) {
    if (retriever != null) {
      retriever.listen(handler);
    }
  }
}
