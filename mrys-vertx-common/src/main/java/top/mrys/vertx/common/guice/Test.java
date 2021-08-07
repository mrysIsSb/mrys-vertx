package top.mrys.vertx.common.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import top.mrys.cloud.ali.demo.GatewayBoot;
import top.mrys.vertx.common.ChannelContext;

/**
 * @author mrys
 * @date 2021/7/30
 */
public class Test {

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(Stage.PRODUCTION, new MyModule());
    Injector injector1 = injector.createChildInjector(new MyModule2());
    injector.getElements().forEach(System.out::println);
    System.out.println("-----------------------------------------------------");
    injector1.getElements().forEach(System.out::println);
  }

}

class MyModule extends AbstractModule {


  @Override
  protected void configure() {
    bind(ChannelContext.class);
  }
}
class MyModule2 extends AbstractModule {


}