package top.mrys.vertx.demo;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;

/**
 * @author mrys
 * 2021/6/28
 */
public class Boot extends Launcher {

  /**
   * Hook for sub-classes of {@link Launcher} after the vertx instance is started.
   *
   * @param vertx the created Vert.x instance
   */
  @Override
  public void afterStartingVertx(Vertx vertx) {
    ShellService service = ShellService.create(vertx,
            new ShellServiceOptions().setTelnetOptions(
                    new TelnetTermOptions().
                            setHost("localhost").
                            setPort(4000)
            )
    );
    service.start();
    System.out.println("-----------------");
  }

  public static void main(String[] args) {
    new Boot().dispatch(new String[]{"run","top.mrys.vertx.demo.MainVerticle"});
  }
}
