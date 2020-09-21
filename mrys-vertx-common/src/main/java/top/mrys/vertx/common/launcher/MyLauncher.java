package top.mrys.vertx.common.launcher;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.impl.Parseable;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import top.mrys.vertx.common.config.ConfigCentreStoreFactory;
import top.mrys.vertx.common.config.ConfigRepo;
import top.mrys.vertx.common.utils.VertxHolder;

/**
 * @author mrys
 * @date 2020/7/21
 */
@Slf4j
@Data
public class MyLauncher extends AbstractVerticle {

  private final static String CONF_PREFIX = "-conf";

  @Deprecated
  public static MyRefreshableApplicationContext context;

  private MyRefreshableApplicationContext applicationContext;
  private String[] args;
  private Class mainClass;


  private Resource resource;

  private ConfigRepo configRepo;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.info("------------------------------------starting------------------------------------");
    applicationContext.register(mainClass);
    configRepo = new ConfigRepo();
    applicationContext.registerBean("configRepo", ConfigRepo.class, () -> configRepo);
    getBootConfig(args).getConfig()
        .onSuccess(json -> updateConfig(json, startPromise))
        .onFailure(startPromise::fail);
    log.info("------------------------------------started------------------------------------");
  }

  private void updateConfig(JsonObject json, Promise<Void> promise) {
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

  private static ConfigRetriever getBootConfig(String[] args) {
    Vertx tempVertx = Vertx.vertx();
    return ConfigRetriever.create(tempVertx, new ConfigRetrieverOptions()
        .addStore(getBootOptions())
        .addStore(getArgsConfigStoreOptions(args)));//合并，相同保留后
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


  private static void addShutdownHook(VertxImpl vertx) {
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      CountDownLatch latch = new CountDownLatch(1);
      vertx.close().onComplete(event -> latch.countDown());
      try {
        System.out.println("vertx ---- stop111");
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

  private static VertxImpl getVertxInstance() {
    VertxOptions options = new VertxOptions();
    VertxImpl vertx = (VertxImpl) Vertx.vertx(options);
    VertxHolder.setMainVertx(vertx);
    vertxAddCloseHook(vertx);
    addShutdownHook(vertx);
    return vertx;
  }
  //---------------------------------

  public static void run(Class mainClass, String[] args, Promise<ApplicationContext> handler) {
    MyRefreshableApplicationContext context = new MyRefreshableApplicationContext();
    context.addScanPackage("top.mrys.vertx.common");
    VertxImpl vertx = getVertxInstance();
    context.registerBean(Vertx.class, () -> vertx);
    MyLauncher verticle = new MyLauncher();
    verticle.setApplicationContext(context);
    verticle.setArgs(args);
    verticle.setMainClass(mainClass);
    vertx.deployVerticle(verticle, event -> {
      if (event.succeeded()) {
        context.registerShutdownHook();
        log.info("refresh");
        context.refresh();
        log.info("refreshed");
        MyLauncher.context = context;
        handler.complete(context);
      } else {
        log.error("启动失败", event.cause());
        handler.fail(event.cause());
      }
    });
  }

  public static Future<ApplicationContext> run(Class mainClass, String[] args) {
    Promise<ApplicationContext> promise = Promise.promise();
    run(mainClass, args, promise);
    return promise.future();
  }

  @SneakyThrows
  public static void onlyRun(Class clazz, String[] args) {
    run(clazz, args, Promise.promise());
   /* MyRefreshableApplicationContext context = new MyRefreshableApplicationContext();
    context.addScanPackage("top.mrys.vertx.common");
    VertxImpl vertx = getVertxInstance();
    context.registerBean(Vertx.class, () -> vertx);
    MyLauncher verticle = new MyLauncher();
    verticle.setApplicationContext(context);
    verticle.setArgs(args);
    verticle.setMainClass(clazz);
    vertx.deployVerticle(verticle,event -> {
      if (event.succeeded()) {
        String result = event.result();
        System.out.println(result);
        context.registerShutdownHook();
        context.refresh();
        MyLauncher.context = context;
      } else {
        log.error("启动失败", event.cause());
      }
    });*/

    /*ConfigRetriever retriever = getConfigRetriever(args);
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
        });*/

    /*return context;*/
  }
  //--------------------------------------------
}
