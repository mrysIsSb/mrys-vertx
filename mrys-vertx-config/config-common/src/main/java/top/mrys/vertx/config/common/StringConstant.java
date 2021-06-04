package top.mrys.vertx.config.common;

/**
 * @author mrys
 * 2021/5/26
 */
public interface StringConstant {

  //获取config
  String request_event_bus_address = "_config.get";

  //提示有config 更新
  String config_update = "_config.update";

  String event_bus_type = "mrys-event-bus";

  String nacos_type = "mrys-nacos";
}
