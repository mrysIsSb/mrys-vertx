package top.mrys.vertx.springboot;

import io.vertx.core.json.JsonObject;
import org.springframework.core.env.PropertySource;

/**
 * @author mrys
 * @date 2020/10/29
 */
public class JsonPropertySource extends PropertySource<JsonObject> {

  private JsonObject source;


  public JsonPropertySource(String name, JsonObject source) {
    super(name, source);
    this.source = source;
  }

  @Override
  public Object getProperty(String name) {
    return getSource().getValue(name);
  }

  public void setSource(JsonObject source) {
    this.source = source;
  }

  /**
   * Return the underlying source object for this {@code PropertySource}.
   */
  @Override
  public JsonObject getSource() {
    return source;
  }
}
