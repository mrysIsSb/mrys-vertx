package top.mrys.vertx.common.config;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置访问器
 *
 * @author mrys
 * @date 2020/10/28
 */
@Slf4j
public class ConfigLoader {

  /**
   * on config data update event key
   *
   * @author mrys
   */
  private String dataUpdateKey = "config.data.update";

  public ConfigRepo load() {
    return ConfigRepo.getInstance();
  }

  public JsonObject getConfig() {
    return load().getData();
  }

  public void updateConfig(JsonObject data) {
    load().mergeInData(data).resolve();
    Vertx owner = Vertx.currentContext().owner();
    if (owner != null) {
      owner.eventBus().publish(dataUpdateKey, getConfig());
    }
  }

  public String[] getActiveProfiles() {
    return load().getProfilesActive();
  }


  public <T> List<T> getArrForKey(String key, Class<T> clazz) {
    return load().getArrForKey(key, clazz);
  }

  public void show(String prefix) {
    log.info("{}-{}", prefix, getConfig());
  }

  public Object getByPath(String name) {
    return load().getForPath(name, Object.class);
  }
}
