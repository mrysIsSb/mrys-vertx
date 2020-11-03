package top.mrys.vertx.common.config;

import java.util.List;
import lombok.Data;

/**
 * auth: "123456yj"
 *       key: "config:"${profiles.active}
 *       type: "SENTINEL"
 *       masterName: "mymaster"
 *       role: "MASTER"
 *       maxPoolSize: 32
 *       maxPoolWaiting: 128
 *       endpoints: [
 *         "redis://192.168.124.16:26381"
 *         "redis://192.168.124.16:26382"
 *         "redis://192.168.124.16:26383"
 *       ]
 * @author mrys
 * @date 2020/11/3
 */
@Data
public class RedisConfigCentrePropertyOptions extends AbstractConfigCentrePropertyOptions<RedisConfigCentrePropertyOptions>{
  private String auth;
  private String key;
  private String type;
  private String role;
  private int maxPoolSize=8;
  private int maxPoolWaiting=32;
  private List<String> endpoints;

}
