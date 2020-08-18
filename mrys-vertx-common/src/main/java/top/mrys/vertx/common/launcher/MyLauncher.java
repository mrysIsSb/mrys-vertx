package top.mrys.vertx.common.launcher;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import top.mrys.vertx.common.config.ConfigCentreStoreFactory;
import top.mrys.vertx.common.utils.MyJsonUtil;
import top.mrys.vertx.common.utils.VertxHolder;

/**
 * @author mrys
 * @date 2020/7/21
 */
@Slf4j
public class MyLauncher {

  private final static String CONF_PREFIX = "-conf";

  public static MyRefreshableApplicationContext context;

  @SneakyThrows
  public static ApplicationContext run(Class clazz, String[] args) {
    MyRefreshableApplicationContext context = new MyRefreshableApplicationContext();
    context.register(clazz);
    context.registerBean(Vertx.class, MyLauncher::getVertx);

    ConfigRetriever retriever = getConfigRetriever(args);
    retriever.listen(event -> {
      JsonObject json = event.getNewConfiguration();
      context.registerBean("config", JsonObject.class, () -> json);
      context.refreshIfActive();
    });
    retriever.getConfig()
        .onSuccess(json -> {
          String active = MyJsonUtil.getByPath(json.toString(), "profiles.active", String.class);
          JsonObject configCentre = json.getJsonObject("configCentre");
          if (StringUtils.hasText(active)) {
            ConfigRetriever retriever1 = getConfigRetriever(args, new ConfigStoreOptions()
                .setType(ConfigCentreStoreFactory.configCentre)
                .setConfig(new JsonObject().put("active", active)
                    .mergeIn(configCentre)
                ));
            retriever1.getConfig()
                .onSuccess(json1 -> {
                  System.out.println(json1.toString());
                  context.registerBean("config", JsonObject.class, () -> json1);
                  context.refreshIfActive();
                });
            retriever1.listen(event -> {
              JsonObject json1 = event.getNewConfiguration();
              System.out.println(json1.toString());
              context.registerBean("config", JsonObject.class, () -> json1);
              context.refreshIfActive();
            });
          }
          context.registerBean("config", JsonObject.class, () -> json);
          context.refreshIfActive();
        });

    context.registerShutdownHook();
    context.refresh();
    MyLauncher.context = context;
    return context;
  }

  public static VertxImpl getVertx() {
    VertxOptions options = new VertxOptions();
    VertxImpl vertx = (VertxImpl) Vertx.vertx(options);
    VertxHolder.setMainVertx(vertx);
    vertxAddCloseHook(vertx);
    addShutdownHook(vertx);
    return vertx;
  }

  private static ConfigRetriever getConfigRetriever(String[] args,ConfigStoreOptions...  other) {
    Vertx tempVertx = Vertx.vertx();
    ConfigRetrieverOptions op = getConfigRetrieverOptions(args);
    if (ArrayUtil.isNotEmpty(other)) {
      for (ConfigStoreOptions options : other) {
        op.addStore(options);
      }
    }
    return ConfigRetriever.create(tempVertx, op);
  }

  private static ConfigRetrieverOptions getConfigRetrieverOptions(String[] args) {
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
    return new ConfigRetrieverOptions()
        .addStore(new ConfigStoreOptions()
            .setType("file")
            .setFormat("json")
            .setOptional(true)
            .setConfig(
                new JsonObject()
                    .put("path", "conf" + File.separator + "config.json"))
        ).addStore(
            new ConfigStoreOptions()
                .setType("json")
                .setConfig(json)
        );
  }


  private static void addShutdownHook(VertxImpl vertx) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      CountDownLatch latch = new CountDownLatch(1);
      vertx.close().onComplete(event -> latch.countDown());
      try {
        latch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }));
  }

  private static void vertxAddCloseHook(VertxImpl vertx) {
    vertx.addCloseHook(completion -> {
      System.out.println("vertx ---- stop");
      completion.complete();
    });
  }
}
