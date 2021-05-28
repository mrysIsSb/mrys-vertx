package top.mrys.vertx.word2vec;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
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
    NDManager manager = NDManager.newBaseManager();
    NDArray ones = manager.ones(new Shape(2, 3));
    System.out.println(ones.max());
    System.out.println("----------");
  }
}
