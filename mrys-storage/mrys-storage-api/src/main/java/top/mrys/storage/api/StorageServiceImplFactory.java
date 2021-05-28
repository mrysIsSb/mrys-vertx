package top.mrys.storage.api;

/**
 * @author mrys
 * 2021/5/28
 */
public interface StorageServiceImplFactory<D, ID> {

  StorageApi<D, ID> getStorageApi();
}
