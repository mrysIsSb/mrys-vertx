package top.mrys.vertx.common.launcher;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.json.JsonObject;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.utils.AnnotationUtil;
import top.mrys.vertx.common.utils.TypeUtil;
import top.mrys.vertx.common.utils.VertxHolder;

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
    VertxHolder.setMainVertx(vertx);
    vertxAddCloseHook(vertx);
    addShutdownHook(vertx);
    Annotation[] annotations = clazz.getAnnotations();
    if (ArrayUtil.isNotEmpty(annotations)) {
      for (Annotation annotation : annotations) {
        Enable enable = annotation.annotationType().getAnnotation(Enable.class);
        if (Objects.nonNull(enable)) {
          Class starter = enable.value();
          if (Starter.class.isAssignableFrom(starter)) {
            Starter o = (Starter) starter.newInstance();
            o.start(annotation);
          }
        }
      }
    }
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
