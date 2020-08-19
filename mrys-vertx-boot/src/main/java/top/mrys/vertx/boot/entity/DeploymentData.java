package top.mrys.vertx.boot.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author mrys
 * @date 2020/8/19
 */
@Data
@Accessors(chain = true)
public class DeploymentData {

  private String deployId;
}
