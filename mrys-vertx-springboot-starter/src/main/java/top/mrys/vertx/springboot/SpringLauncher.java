package top.mrys.vertx.springboot;

import io.vertx.core.Launcher;

/**
 * @author mrys
 * 2021/6/28
 */
public class SpringLauncher extends Launcher {

  @Override
  protected String getMainVerticle() {
    return MainVerticle.class.getName();
  }
}
