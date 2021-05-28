package top.mrys.vertx.word2vec;

import io.vertx.core.json.JsonObject;
import top.mrys.storage.api.StorageApi;
import top.mrys.storage.api.StorageServiceImplFactory;
import top.mrys.storage.api.StorageSpi;

/**
 * @author mrys
 * 2021/5/28
 */
public class Boot {
  public static void main(String[] args) {
    StorageServiceImplFactory<?, ?> factory = StorageSpi.get();
    StorageApi<JsonObject, String> api = (StorageApi<JsonObject, String>) factory.getStorageApi();
    System.out.println(api);
  }
}
