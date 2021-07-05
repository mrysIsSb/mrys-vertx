//package top.mrys.vertx.springboot;
//
//import io.vertx.core.Vertx;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//import top.mrys.vertx.common.config.ConfigLoader;
//import top.mrys.vertx.common.config.ConfigVerticle;
//import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
//import top.mrys.vertx.common.factorys.SpringObjectInstanceFactory;
//import top.mrys.vertx.common.launcher.ApplicationContext;
//
///**
// * vertx 相关的创建对象工厂
// *
// * @author mrys
// * @date 2020/10/29
// */
//public class VertxRelevantObjectInstanceFactory {
//
//  private static Vertx vertx;
//
//  /**
//   * 创建vertx
//   *
//   * @author mrys
//   */
//  public synchronized static Vertx createVertx() {
//    if (vertx == null) {
//      vertx = Vertx.vertx();
//      addShutdownHook(vertx);
//    }
//    return vertx;
//  }
//
//  private static void addShutdownHook(Vertx vertx) {
//    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//      CountDownLatch latch = new CountDownLatch(1);
//      vertx.close().onComplete(event -> latch.countDown());
//      try {
//        System.out.println("vertx ---- stop");
//        latch.await(10, TimeUnit.SECONDS);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//    }));
//  }
//
//
//  private static ConfigVerticle configVerticle = new ConfigVerticle();
//  private static ApplicationContext applicationContext = new ApplicationContext();
//  private static ObjectInstanceFactory factory = new SpringObjectInstanceFactory();
//  private static ConfigLoader configLoader = new ConfigLoader();
//
//  static {
//    configVerticle.setContext(context());
//    applicationContext.setInstanceFactory(factory);
//    applicationContext.setConfigLoader(configLoader);
//  }
//
//  /**
//   * 创建 configVerticle
//   *
//   * @author mrys
//   */
//  public static ConfigVerticle createConfigVerticle() {
//    return configVerticle;
//  }
//
//  public static ApplicationContext context() {
//    return applicationContext;
//  }
//
//  public static ObjectInstanceFactory objectInstanceFactory() {
//    return factory;
//  }
//
//  public static ConfigLoader getConfigLoader() {
//    return configLoader;
//  }
//
//}
