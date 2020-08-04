package top.mrys.vertx.common.launcher;

import cn.hutool.json.JSONUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
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
    ConfigRetriever retriever = getConfigRetriever(args);
    retriever.listen(event -> {
      JsonObject json = event.getNewConfiguration();
      context.registerBean("config", JsonObject.class, () -> json);
      context.refresh();
    });
    retriever.getConfig()
        .onSuccess(json -> {
          context.registerBean("config", JsonObject.class, () -> json);
          context.refresh();
        });
    context.registerBean(Vertx.class, MyLauncher::getVertx);

    context.registerShutdownHook();
    context.refresh();
    /*vertx.setPeriodic(10000, event -> {
      Vertx bean = null;
      try {
        bean = context.getBean(Vertx.class);

      } catch (BeansException e) {
      }
      if (Objects.isNull(bean)) {
        log.info("add");
        context.registerBean(Vertx.class, MyLauncher::getVertx);
      } else {
        log.info("remove");
        bean.close();
        context.removeBean(Vertx.class);
      }
      log.info("refresh");

      context.refresh();
    });*/
    MyLauncher.context = context;
    return context;
  }

  public static VertxImpl getVertx() {
    VertxImpl vertx = (VertxImpl) Vertx.vertx(new VertxOptions());
    VertxHolder.setMainVertx(vertx);
    vertxAddCloseHook(vertx);
    addShutdownHook(vertx);
    return vertx;
  }

  private static ConfigRetriever getConfigRetriever(String[] args) {
    Vertx tempVertx = Vertx.vertx();
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
    ConfigRetrieverOptions op = new ConfigRetrieverOptions()
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

    return ConfigRetriever.create(tempVertx, op);
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
