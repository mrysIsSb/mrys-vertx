package top.mrys.vertx.common.config;

import com.typesafe.config.Config;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import lombok.Getter;
import lombok.Setter;

/**
 * 配置加载器
 *
 * @author mrys
 * @date 2020/10/28
 */
public class ConfigLoader {

  @Getter
  @Setter
  private Vertx vertx;


  public ConfigRepo load() {
    EventBus eventBus = vertx.eventBus();
    eventBus.
    return ConfigRepo.getInstance();
  }


}
