package top.mrys.vertx.common.config;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.impl.Parseable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.common.utils.MyJsonUtil;

/**
 * @author mrys
 * @date 2020/9/12
 */
@Slf4j
public class ConfigRepo {

  private static final ConfigRepo configRepo = new ConfigRepo();


  @Getter
  @Setter
  private JsonObject data = new JsonObject();

  private ConfigRepo() {
  }

  public static ConfigRepo getInstance() {
    return configRepo;
  }


  public String[] getProfilesActive() {
    return MyJsonUtil.getByPath(data.toString(), "profiles.active", String[].class);
  }

  public ConfigRepo resolve() {
    String j = data.toString().replaceAll("\"(\\w*\\$\\{[\\w.]*\\}\\w*)\"", "$1");
    Config resolve = Parseable.newString(j, ConfigParseOptions.defaults())
        .parse().toConfig().resolve();
    Map<String, Object> map = resolve.root().unwrapped();
    return mergeInData(new JsonObject(map));
  }

  /**
   * 将新的数据合并进来 相同保留后
   *
   * @author mrys
   */
  public ConfigRepo mergeInData(JsonObject json) {
    data = data.mergeIn(json, true);
    return this;
  }


  public <T> T getForClass(Class<T> clazz) {
    String simpleName = clazz.getSimpleName();
    return getForPath(simpleName, clazz);
  }

  public <T> T getForPath(String key, Class<T> clazz) {
    return getForPath(key, clazz, null);
  }

  public <T> T getForPath(String key, Class<T> clazz, T def) {
    if (data.isEmpty()) {
      return null;
    }
    T o = null;
    JSONObject jsonObject = JSONUtil.parseObj(data.toString());
    if (JsonObject.class.equals(clazz)) {
      Map map = jsonObject.getByPath(key, Map.class);
      o = (T) new JsonObject(map);
    } else {
      o = jsonObject.getByPath(key, clazz);
    }
    if (Objects.isNull(o)) {
      return def;
    }
    return o;
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
    if (jsonArray == null || jsonArray.isEmpty()) {
      return null;
    }
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
