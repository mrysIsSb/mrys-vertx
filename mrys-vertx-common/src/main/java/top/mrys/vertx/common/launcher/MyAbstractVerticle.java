package top.mrys.vertx.common.launcher;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import lombok.Getter;

/**
 * @author mrys
 * @date 2020/8/18
 */
public class MyAbstractVerticle extends AbstractVerticle {
  @Getter
  private final DeploymentOptions deploymentOptions=new DeploymentOptions();

}
