package top.mrys.storage.api;

import java.util.ServiceLoader;

/**
 * @author mrys
 * 2021/5/28
 */
public class StorageSpi {
  public static StorageServiceImplFactory<?, ?> get() {
    ServiceLoader<StorageServiceImplFactory> s = ServiceLoader.load(StorageServiceImplFactory.class,
            Thread.currentThread().getContextClassLoader());
    if (s.iterator().hasNext()) {
      return s.iterator().next();
    }
    return null;
  }
}
