package top.mrys.storage.map;

import io.vertx.core.json.JsonObject;
import top.mrys.storage.api.StorageApi;
import top.mrys.storage.api.StorageServiceImplFactory;

/**
 * @author mrys
 * 2021/5/28
 */
public class MapStorageFactory implements StorageServiceImplFactory<JsonObject, String> {
  @Override
  public StorageApi<JsonObject, String> getStorageApi() {
    return new MapStorageImpl();
  }
}
