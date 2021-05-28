package top.mrys.storage.api;

/**
 * @author mrys
 * 2021/5/28
 */
public interface StorageApi<D, ID> {
  D getById(ID id);
}
