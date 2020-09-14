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


  public void mergeToData(JsonObject json) {
    data = data.mergeIn(json, true);
  }

  public <T> T getForClass(Class<T> clazz) {
    String simpleName = clazz.getSimpleName();
    if (data.isEmpty()) {
      return null;
    }
    JsonObject jsonObject = data.getJsonObject(simpleName);
    return jsonObject.mapTo(clazz);
  }

  public <T> List<T> getArrForClass(Class<T> clazz) {
    String simpleName = clazz.getSimpleName();
    if (data.isEmpty()) {
      return null;
    }
    JsonArray jsonArray = data.getJsonArray(simpleName);
    return jsonArray.stream().map(o -> (JsonObject) o).map(o -> o.mapTo(clazz))
        .collect(Collectors.toList());
  }
}
