package top.mrys.vertx.common.launcher;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mrys
 * @date 2020/7/21
 */
@Slf4j
public class MyLauncher {

  private final static String CONF_PREFIX = "-conf";


  @SneakyThrows
  public static Vertx run(Class clazz, String[] args) {
    ConfigRetriever retriever = getConfigRetriever(args);
    retriever.getConfig()
        .onSuccess(json -> System.out.println(json.toString()));
    VertxImpl vertx = (VertxImpl) Vertx.vertx(new VertxOptions());
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
