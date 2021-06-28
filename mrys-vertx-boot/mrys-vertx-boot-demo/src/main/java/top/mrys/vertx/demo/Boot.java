package top.mrys.vertx.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import sun.applet.Main;

import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;

/**
 * @author mrys
 * 2021/6/28
 */
@SpringBootApplication
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
                    new TelnetTermOptions()
                            .setHost("localhost")
                            .setPort(4000)
            )
    );
    service.start();
    System.out.println("-----------------");
  }

  @Override
  protected String getMainVerticle() {
    return "top.mrys.vertx.demo.MainVerticle";
  }

  public static void main(String[] args) {
    ConfigurableApplicationContext run = SpringApplication.run(Boot.class, args);
    for (String name : run.getBeanDefinitionNames()) {
      System.out.println(name);
    }
    System.out.println("-------------------------");
    GenericApplicationContext context = new GenericApplicationContext();
    context.setParent(run);
    context.registerBean(MainVerticle.class);
    context.refresh();
    for (String name : context.getBeanDefinitionNames()) {
      System.out.println(name);
    }
    System.out.println(context.getBean(Boot.class));
    System.out.println(context.getBean(MainVerticle.class));
    System.out.println("-------------------------");
    context.close();
    for (String name : context.getBeanDefinitionNames()) {
      System.out.println(name);
    }
    new Boot().dispatch(args);
  }
}
