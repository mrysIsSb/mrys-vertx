package top.mrys.vertx.common.launcher;

import io.vertx.core.Launcher;
import lombok.SneakyThrows;

/**
 * @author mrys
 * @date 2020/7/21
 */
public class MyLauncher extends Launcher {

  @SneakyThrows
  public static Launcher run(Class clazz, String[] args) {
    MyLauncher launcher = new MyLauncher();
    launcher.dispatch(clazz.newInstance(),args);
    return launcher;
  }
}
