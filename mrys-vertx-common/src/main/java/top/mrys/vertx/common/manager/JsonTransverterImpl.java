package top.mrys.vertx.common.manager;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonGeneratorImpl;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author mrys
 * @date 2020/8/19
 */
@Component
public class JsonTransverterImpl implements JsonTransverter {

  private static final ObjectMapper mapper = DatabindCodec.mapper().copy();
  private static final ObjectMapper prettyMapper = DatabindCodec.prettyMapper().copy();

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
    mapper.registerModule(module);
    prettyMapper.registerModule(module);
  }


  @SneakyThrows
  @Override
  public <R> R deSerialize(String o, Class<R> rClass) {
    if (StrUtil.isBlank(o)) {
      return null;
    }
//    if (JSONUtil.isJsonObj(o)) {
      return mapper.readValue(o, rClass);
  /*  } else if (JSONUtil.isJsonArray(o)) {
      JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, rClass);
      return mapper.readValue(o, javaType);
    }
    return null;*/
  }

  @SneakyThrows
  @Override
  public String serialize(Object o) {
    return mapper.writeValueAsString(o);
  }
}
