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
import java.util.Iterator;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author mrys
 * @date 2020/8/8
 */
public class MyRedisConfigStore implements ConfigStore {

  private final Context ctx;
  private final Redis redis;
  private final String field;
  private final String auth;
  private boolean closed;

  public MyRedisConfigStore(Vertx vertx, JsonObject config) {
    this.ctx = vertx.getOrCreateContext();
    this.field = config.getString("key", "configuration");
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
    connection.send(Request.cmd(Command.HGETALL).arg(this.field),
        rep -> completionHandler.handle(rep.map(response -> {
          JSONObject json = JSONUtil.parseObj("{}");
          Iterator it = response.iterator();
          while (it.hasNext()) {
            String key = it.next().toString();
            String value = it.next().toString();
            JSONUtil.putByPath(json, key, value);
          }
          return new JsonObject(json.toString()).toBuffer();
        })));
  }

}
