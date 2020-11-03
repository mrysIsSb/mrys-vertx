package top.mrys.vertx.common.config;

import lombok.Data;

/**
 * @author mrys
 * @date 2020/11/3
 */
@Data
public class ConfigCentreStoreOptions<T extends AbstractConfigCentrePropertyOptions> {

  /**
   * 仓库类型
   *
   * @author mrys
   */
  private String storeType;

  /**
   *  和storeType 2选1 优先 这个
   * @author mrys
   */
  private Class<T> optionClass;

  private T config;

}
