package top.mrys.vertx.common.config;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import top.mrys.vertx.common.utils.MyJsonUtil;

/**
 * @author mrys
 * @date 2020/9/12
 */
public class ConfigRepo {

  private String version;

  @Getter
  @Setter
  private JsonObject data = new JsonObject();

  public String getProfilesActive() {
    return MyJsonUtil.getByPath(data.toString(), "profiles.active", String.class);
  }

  /**
   * 将新的数据合并进来 相同保留后
   *
   * @author mrys
   */
  public void mergeInData(JsonObject json) {
    data = data.mergeIn(json, true);
  }

  public <T> T getForClass(Class<T> clazz) {
    String simpleName = clazz.getSimpleName();
    return getForKey(simpleName, clazz);
  }

  public <T> T getForKey(String key, Class<T> clazz) {
    if (data.isEmpty()) {
      return null;
    }
    JsonObject jsonObject = data.getJsonObject(key);
    if (clazz.isAssignableFrom(JsonObject.class)) {
      return (T) jsonObject;
    } else {
      return jsonObject.mapTo(clazz);
    }
  }

  public <T> List<T> getArrForClass(Class<T> clazz) {
    String simpleName = clazz.getSimpleName();
    return getArrForKey(simpleName, clazz);
  }

  public <T> List<T> getArrForKey(String key, Class<T> clazz) {
    if (data.isEmpty()) {
      return null;
    }
    JsonArray jsonArray = data.getJsonArray(key);
    return jsonArray.stream().map(o -> (JsonObject) o).map(o -> {
      if (clazz.isAssignableFrom(JsonObject.class)) {
        return (T) o;
      } else {
        return o.mapTo(clazz);
      }
    })
        .collect(Collectors.toList());
  }
}
