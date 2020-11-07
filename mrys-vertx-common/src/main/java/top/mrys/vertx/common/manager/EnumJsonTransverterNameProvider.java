package top.mrys.vertx.common.manager;

/**
 * @author mrys
 * @date 2020/11/6
 */
public enum EnumJsonTransverterNameProvider implements JsonTransverterNameProvider {
  http_server;

  public String getJsonTransverterName() {
    return name();
  }
}
