//package top.mrys.vertx.springboot;
//
//import io.vertx.core.json.JsonObject;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.env.PropertySource;
//import top.mrys.vertx.common.config.ConfigLoader;
//
///**
// * @author mrys
// * @date 2020/10/29
// */
//@Slf4j
//public class VertxPropertySource extends PropertySource<JsonObject> {
//
//  private ConfigLoader configLoader;
//
//  public VertxPropertySource(String name) {
//    super(name);
//  }
//
//  public static VertxPropertySource getInstance(String name, ConfigLoader configLoader) {
//    VertxPropertySource vertxPropertySource = new VertxPropertySource(name);
//    vertxPropertySource.configLoader = configLoader;
//    return vertxPropertySource;
//  }
//
//  @Override
//  public Object getProperty(String name) {
//    return configLoader.getByPath(name);
//  }
//
//  public void setSource(JsonObject source) {
//    configLoader.updateConfig(source);
//  }
//
//  @Override
//  public JsonObject getSource() {
//    return configLoader.getConfig();
//  }
//}
