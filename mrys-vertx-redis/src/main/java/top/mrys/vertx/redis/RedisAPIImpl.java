package top.mrys.vertx.redis;

import io.vertx.core.Future;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Response;

/**
 * @author mrys
 * @date 2021/1/13
 */
public class RedisAPIImpl implements RedisAPI {

  @Override
  public void close() {

  }

  /**
   * Send untyped command to redis.
   *
   * @param cmd  the command
   * @param args var args
   * @return Future response.
   */
  @Override
  public Future<Response> send(Command cmd, String... args) {
    return null;
  }
}
