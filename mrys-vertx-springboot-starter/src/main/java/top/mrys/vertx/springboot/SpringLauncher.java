package top.mrys.vertx.springboot;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;

/**
 * @author mrys 2021/6/28
 */
public class SpringLauncher extends Launcher {

  private final CustomVerticleFactory customVerticleFactory;

  public SpringLauncher(CustomVerticleFactory customVerticleFactory) {
    this.customVerticleFactory = customVerticleFactory;
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
//    ShellService service = ShellService.create(vertx,
//        new ShellServiceOptions().setTelnetOptions(
//            new TelnetTermOptions()
//                .setHost("localhost")
//                .setPort(4000)
//        )
//    );
//    service.start();
    vertx.registerVerticleFactory(customVerticleFactory);
    System.out.println("-----------------");
  }

  /**
   * main Verticle
   *
   * @author mrys
   */
  @Override
  protected String getMainVerticle() {
    return CustomVerticleFactory.prefixName + ":mainVerticle";
  }

}
