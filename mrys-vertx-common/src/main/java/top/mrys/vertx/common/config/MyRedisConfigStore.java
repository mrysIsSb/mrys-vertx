package top.mrys.vertx.common.config;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigValueFactory;
import com.typesafe.config.impl.Parseable;
import io.vertx.config.spi.ConfigStore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.RedisOptions;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import top.mrys.vertx.common.factorys.JsonTransverterFactory;
import top.mrys.vertx.common.manager.EnumJsonTransverterNameProvider;
import top.mrys.vertx.common.manager.JsonTransverterNameProvider;

/**
 * @author mrys
 * @date 2020/8/8
 */
@Slf4j
public class MyRedisConfigStore implements ConfigStore {

  private final Context ctx;
  private final Redis redis;
  //  private final String field;
  private final String[] keys;
  private final String auth;
  private boolean closed;

  public MyRedisConfigStore(Vertx vertx, JsonObject config) {
    this.ctx = vertx.getOrCreateContext();
//    this.field = config.getString("key", "configuration");
    this.keys = (String[]) config.getJsonArray("keys").getList().stream().toArray(String[]::new);
    this.auth = config.getString("auth");
    this.redis = Redis.createClient(vertx, new RedisOptions(config));
  }

  public void close(Handler<Void> completionHandler) {
    if (Vertx.currentContext() == this.ctx) {
      if (!this.closed) {
        this.closed = true;
        this.redis.close();
      }
      completionHandler.handle(null);
    } else {
      this.ctx.runOnContext((v) -> {
        this.close(completionHandler);
      });
    }

  }

  @Override
  public void get(Handler<AsyncResult<Buffer>> completionHandler) {
    if (Vertx.currentContext() == this.ctx) {
      this.redis.connect(event -> {
        if (event.succeeded()) {
          RedisConnection connection = event.result();
          if (StringUtils.hasText(this.auth)) {
            connection.send(Request.cmd(Command.AUTH).arg(this.auth)).onSuccess(event1 -> {
              get(connection, completionHandler);
            });
          } else {
            get(connection, completionHandler);
          }
        }
      });
    } else {
      this.ctx.runOnContext((v) -> this.get(completionHandler));
    }
  }

  private void get(RedisConnection connection, Handler<AsyncResult<Buffer>> completionHandler) {
    List<Request> commands = Arrays.stream(keys).map(s -> Request.cmd(Command.HGETALL).arg(s))
        .collect(Collectors.toList());
    connection.batch(commands, rep -> {
      try {
        completionHandler
            .handle(rep.map(responses -> responses.stream().map(this::getResponseBufferFunction)
                .reduce((jsonObject, jsonObject2) -> jsonObject.mergeIn(jsonObject2, true))
                .get().toBuffer()
            ));
      }finally {
        connection.close();
      }
    });

  }

  private JsonObject getResponseBufferFunction(Response response) {
    JSONObject json = JSONUtil.parseObj("{}");
//    log.debug("redis:{}",response.toString());
    Iterator it = response.iterator();
    while (it.hasNext()) {
      String key = it.next().toString();
      String value = it.next().toString();
      if (JSONUtil.isJson(value)) {
        JSONUtil.putByPath(json, key, JsonTransverterFactory.getJsonTransverter(
            EnumJsonTransverterNameProvider.config_redis).deSerialize(value, HashMap.class));//支持value为json
      } else {
        JSONUtil.putByPath(json, key, value);
      }
    }
    return new JsonObject(json.toString());
  }

}
