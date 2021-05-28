package top.mrys.storage.map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.vertx.core.json.JsonObject;
import top.mrys.storage.api.StorageApi;

/**
 * @author mrys
 * 2021/5/28
 */
public class MapStorageImpl implements StorageApi<JsonObject, String> {

  private ConcurrentMap<String, JsonObject> data = new ConcurrentHashMap<>();

  @Override
  public JsonObject getById(String id) {
    return data.get(id);
  }
}
