package top.mrys.vertx.common.manager;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author mrys
 * @date 2020/8/19
 */
@Component
public class JsonTransverterImpl implements JsonTransverter {

  private static final ObjectMapper mapper = new ObjectMapper();
  private static final ObjectMapper prettyMapper = new ObjectMapper();

  //  ObjectMapper objectMapper = DatabindCodec.mapper();
  {
    //运行注解
    mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    prettyMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    prettyMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

    SimpleModule module = new SimpleModule();
    // custom types

    mapper.registerModule(module);
    prettyMapper.registerModule(module);
  }


  @SneakyThrows
  @Override
  public <R> R deSerialize(String o, Class<R> rClass) {
    return mapper.readValue(o, rClass);
  }

  @SneakyThrows
  @Override
  public String serialize(Object o) {
    return mapper.writeValueAsString(o);
  }
}
