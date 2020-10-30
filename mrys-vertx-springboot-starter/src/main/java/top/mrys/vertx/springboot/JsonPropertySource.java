package top.mrys.vertx.springboot;

import io.vertx.core.json.JsonObject;
import org.springframework.core.env.PropertySource;

/**
 * @author mrys
 * @date 2020/10/29
 */
public class JsonPropertySource extends PropertySource<JsonObject> {
  
  public JsonPropertySource(String name, JsonObject source) {
    super(name, source);
  }
  
  @Override
  public Object getProperty(String name) {
    return getSource().getValue(name);
  }
}
