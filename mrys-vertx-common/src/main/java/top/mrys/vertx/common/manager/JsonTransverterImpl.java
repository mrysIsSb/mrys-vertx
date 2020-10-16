package top.mrys.vertx.common.manager;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import java.io.IOException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * @author mrys
 * @date 2020/8/19
 */
@Component
public class JsonTransverterImpl implements JsonTransverter {

//  ObjectMapper objectMapper = new ObjectMapper();
  ObjectMapper objectMapper = DatabindCodec.mapper();
  {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    SimpleModule module = new SimpleModule();
    objectMapper.registerModule(module);
  }

  @SneakyThrows
  @Override
  public <R> R deSerialize(String o, Class<R> rClass) {
    return objectMapper.readValue(o,rClass);
  }

  @SneakyThrows
  @Override
  public String serialize(Object o) {
    return objectMapper.writeValueAsString(o);
  }
}
