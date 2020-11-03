package top.mrys.vertx.common.config;

/**
 * @author mrys
 * @date 2020/11/3
 */
public abstract class AbstractConfigCentrePropertyOptions<S extends AbstractConfigCentrePropertyOptions> {

  private String[] profiles;

  public String[] getProfiles() {
    return profiles;
  }

  public S setProfiles(String[] profiles) {
    this.profiles = profiles;
    return (S) this;
  }
}
