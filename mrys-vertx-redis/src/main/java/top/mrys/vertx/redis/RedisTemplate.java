package top.mrys.vertx.redis;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisConnection;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * todo 实现一个单一连接的操作
 *
 * @author mrys
 * @date 2020/11/26
 */
@Slf4j
public class RedisTemplate {

  private Redis redisClient;
  private String auth;

  public RedisTemplate(Redis redisClient, String auth) {
    this.redisClient = redisClient;
    this.auth = auth;
  }

  public RedisTemplate(Redis redis) {
    this.redisClient = redis;
  }

  public Future<RedisConnection> getAccessRedisClient() {
    return redisClient.connect()
        .compose(connection -> {
          if (StrUtil.isNotBlank(auth)) {
            return connection.send(Request.cmd(Command.AUTH).arg(auth))
                .compose(response -> Future.succeededFuture(connection), throwable -> Future
                    .failedFuture("redis 认证失败"));
          } else {
            return Future.succeededFuture(connection);
          }
        }, throwable -> Future.failedFuture("redis 连接失败"));
  }

  private <T> Future<T> exec(Request request, Function<Response, T> successMapper) {
    log.debug(">redis:{}", request.toString());
    return getAccessRedisClient().compose(connection -> {
      try {
        return connection.send(request)
            .onSuccess(event -> log.info("redis:{}", event.toString()))
            .compose(response -> Future.succeededFuture(successMapper.apply(response)));
      } finally {
        connection.close();
      }
    });
  }

  private Request createRequest(Command cmd, String... args) {
    Request request = Request.cmd(cmd);
    for (String arg : args) {
      request.arg(arg);
    }
    return request;
  }
  //  <----------------------------key begin---------------------------->

  /**
   * 该命令用于在 key 存在时删除 key。
   *
   * @return 被删除 key 的数量。
   * @author mrys
   */
  public Future<Integer> del(String... keys) {
    return exec(createRequest(Command.DEL, keys), Response::toInteger);
  }

  /**
   * 序列化给定 key ，并返回被序列化的值。
   *
   * @return 如果 key 不存在，那么返回 nil 。 否则，返回序列化之后的值。
   * @author mrys
   */
  public Future<String> dump(String key) {
    return exec(Request.cmd(Command.DUMP).arg(key), Response::toString);
  }

  /**
   * 检查给定 key 是否存在
   *
   * @return 若 key 存在返回 1 ，否则返回 0 。
   * @author mrys
   */
  public Future<Integer> exists(String key) {
    return exec(Request.cmd(Command.EXISTS).arg(key), Response::toInteger);
  }

  /**
   * 为给定 key 设置过期时间，以秒计。
   *
   * @return 设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 。
   * @author mrys
   */
  public Future<Integer> expire(String key, Integer second) {
    return exec(Request.cmd(Command.EXPIRE).arg(key).arg(second), Response::toInteger);
  }

  /**
   * EXPIREAT 的作用和 EXPIRE 类似，都用于为 key 设置过期时间。
   * <p>
   * 不同在于 EXPIREAT 命令接受的时间参数是 UNIX 时间戳(unix timestamp)。 秒
   *
   * @return 设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时
   * <p>
   * (比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 。
   * @author mrys
   */
  public Future<Integer> expireat(String key, Integer timestamp) {
    return exec(Request.cmd(Command.EXPIREAT).arg(key).arg(timestamp), Response::toInteger);
  }

  /**
   * 毫秒
   *
   * @author mrys
   */
  public Future<Integer> expireat(String key, Long timestamp) {
    return expireat(key, Convert.toInt(TimeUnit.MILLISECONDS.toSeconds(timestamp)));
  }

  public Future<Integer> expireat(String key, LocalDateTime time) {
    return expireat(key, time.getSecond());
  }

  /**
   * 设置 key 的过期时间以毫秒计。
   *
   * @return 设置成功，返回 1
   * <p>
   * key 不存在或设置失败，返回 0
   * @author mrys
   */
  public Future<Integer> pexpire(String key, Long ms) {
    return exec(Request.cmd(Command.PEXPIRE).arg(key).arg(ms), Response::toInteger);
  }

  /**
   * 设置 key 过期时间的时间戳(unix timestamp) 以毫秒计
   *
   * @return 设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 。
   * @author mrys
   */
  public Future<Integer> pexpireat(String key, Long timestamp) {
    return exec(Request.cmd(Command.PEXPIREAT).arg(key).arg(timestamp), Response::toInteger);
  }


  public Future<Integer> pexpireat(String key, LocalDateTime time) {
    return pexpireat(key, TimeUnit.NANOSECONDS.toMillis(time.getNano()));
  }


  /**
   * 查找所有符合给定模式( pattern)的 key 。
   *
   * @return 符合给定模式的 key 列表 (Array)。
   * @author mrys
   */
  public Future<Set<String>> keys(String pattern) {
    return exec(Request.cmd(Command.KEYS).arg(pattern),
        response -> response.stream().map(Response::toString).collect(
            Collectors.toSet()));
  }

  /**
   * 将当前数据库的 key 移动到给定的数据库 db 当中。
   *
   * @return 移动成功返回 1 ，失败则返回 0 。
   * @author mrys
   */
  public Future<Integer> move(String key, Integer db) {
    return exec(Request.cmd(Command.MOVE).arg(key).arg(db), Response::toInteger);
  }

  /**
   * 移除 key 的过期时间，key 将持久保持。
   *
   * @return 当过期时间移除成功时，返回 1 。 如果 key 不存在或 key 没有设置过期时间，返回 0 。
   * @author mrys
   */
  public Future<Integer> persist(String key) {
    return exec(Request.cmd(Command.PERSIST).arg(key), Response::toInteger);
  }

  /**
   * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
   *
   * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以秒为单位，返回 key 的剩余生存时间。
   * <p>
   * 注意：在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1
   * @author mrys
   */
  public Future<Integer> ttl(String key) {
    return exec(Request.cmd(Command.TTL).arg(key), Response::toInteger);
  }

  /**
   * 以毫秒为单位返回 key 的剩余的过期时间。
   *
   * @return 当 key 不存在时，返回 -2 。 当 key 存在但没有设置剩余生存时间时，返回 -1 。 否则，以毫秒为单位，返回 key 的剩余生存时间。
   * <p>
   * 注意：在 Redis 2.8 以前，当 key 不存在，或者 key 没有设置剩余生存时间时，命令都返回 -1 。
   * @author mrys
   */
  public Future<Long> pttl(String key) {
    return exec(Request.cmd(Command.PTTL).arg(key), Response::toLong);
  }

  /**
   * 从当前数据库中随机返回一个 key
   *
   * @return 当数据库不为空时，返回一个 key 。 当数据库为空时，返回 nil （windows 系统返回 null）。
   * @author mrys
   */
  public Future<String> randomKey() {
    return exec(Request.cmd(Command.RANDOMKEY), Response::toString);
  }

  /**
   * 修改 key 的名称
   *
   * @return 改名成功时提示 OK ，失败时候返回一个错误。
   * <p>
   * 当 OLD_KEY_NAME 和 NEW_KEY_NAME 相同，或者 OLD_KEY_NAME 不存在时，返回一个错误。
   * <p>
   * 当 NEW_KEY_NAME 已经存在时， RENAME 命令将覆盖旧值。
   * @author mrys
   */
  public Future<String> rename(String key, String newKey) {
    return exec(Request.cmd(Command.RENAME), Response::toString);
  }

  /**
   * 仅当 newkey 不存在时，将 key 改名为 newkey 。
   *
   * @return 修改成功时，返回 1 。 如果 NEW_KEY_NAME 已经存在，返回 0 。
   * @author mrys
   */
  public Future<Integer> renamenx(String key, String newKey) {
    return exec(Request.cmd(Command.RENAMENX), Response::toInteger);
  }

  /**
   * 迭代数据库中的数据库键。
   *
   * @param @Nullable pattern
   * @param @Nullable count
   * @return 数组列表
   * @author mrys
   */
  public Future<Map<Integer, String[]>> scan(Integer cursor, String pattern,
       Integer count) {
    Request cmd = Request.cmd(Command.SCAN).arg(cursor);
    if (StrUtil.isNotBlank(pattern)) {
      cmd.arg("MATCH").arg(pattern);
    }
    if (count != null) {
      cmd.arg("COUNT").arg(count);
    }
    return exec(cmd, response -> {
      HashMap<Integer, String[]> map = new HashMap<>();
      map.put(response.get(0).toInteger(),
          response.get(1).stream().map(Response::toString).toArray(String[]::new));
      return map;
    });
  }

  /**
   * 返回 key 的数据类型，数据类型有：
   * <p>
   * none (key不存在) string (字符串) list (列表) set (集合) zset (有序集) hash (哈希表)
   *
   * @author mrys
   */
  public Future<String> type(String key) {
    return exec(Request.cmd(Command.TYPE).arg(key), Response::toString);
  }
//  <----------------------------key end---------------------------->

  //  <----------------------------string begin---------------------------->

  /**
   * Redis SET 命令用于设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型
   *
   * @return 在 Redis 2.6.12 以前版本， SET 命令总是返回 OK 。
   * <p>
   * 从 Redis 2.6.12 版本开始， SET 在设置操作成功完成时，才返回 OK 。
   * @author mrys
   */
  public Future<String> set(String key, String value) {
    return exec(Request.cmd(Command.SET).arg(key).arg(value), Response::toString);
  }

  /**
   * 获取指定 key 的值
   *
   * @return 返回 key 的值，如果 key 不存在时，返回 nil。 如果 key 不是字符串类型，那么返回一个错误。
   * @author mrys
   */
  public Future<String> get(String key) {
    return exec(Request.cmd(Command.GET).arg(key), Response::toString);
  }

  /**
   * 返回 key 中字符串值的子字符
   * <p>
   * redis 127.0.0.1:6379> GETRANGE mykey 0 3
   * <p>
   * "This"
   * <p>
   * redis 127.0.0.1:6379> GETRANGE mykey 0 -1
   * <p>
   * "This is my test key"
   * <p>
   * end 包含
   *
   * @author mrys
   */
  public Future<String> getRange(String key, Integer start, Integer end) {
    return exec(Request.cmd(Command.GETRANGE).arg(key).arg(start).arg(end), Response::toString);
  }

  /**
   * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
   *
   * @return 返回给定 key 的旧值。 当 key 没有旧值时，即 key 不存在时，返回 nil 。
   * <p>
   * 当 key 存在但不是字符串类型时，返回一个错误。
   * @author mrys
   */
  public Future<String> getSet(String key, String value) {
    return exec(Request.cmd(Command.GETSET).arg(key).arg(value), Response::toString);
  }

  /**
   * 对 key 所储存的字符串值，获取指定偏移量上的位(bit)。
   *
   * @return 字符串值指定偏移量上的位(bit)。
   * <p>
   * 当偏移量 OFFSET 比字符串值的长度大，或者 key 不存在时，返回 0 。
   * @author mrys
   */
  public Future<Integer> getBit(String key, Integer offset) {
    return exec(Request.cmd(Command.GETBIT).arg(key).arg(offset), Response::toInteger);
  }

  /**
   * 将值 value 关联到 key ，并将 key 的过期时间设为 seconds (以秒为单位)
   *
   * @return 设置成功时返回 OK 。
   * @author mrys
   */
  public Future<String> setEx(String key, Integer seconds, String value) {
    return exec(Request.cmd(Command.SETEX).arg(key).arg(seconds).arg(value), Response::toString);
  }

  /**
   * 只有在 key 不存在时设置 key 的值。
   *
   * @return 设置成功，返回 1 。 设置失败，返回 0 。
   * @author mrys
   */
  public Future<Integer> setNx(String key, String value) {
    return exec(Request.cmd(Command.SETNX).arg(key).arg(value), Response::toInteger);
  }


  /**
   * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。
   *
   * @return 被修改后的字符串长度。
   * @author mrys
   */
  public Future<Integer> setRange(String key, Integer offset, String value) {
    return exec(Request.cmd(Command.SETRANGE).arg(key).arg(offset).arg(value), Response::toInteger);
  }

  /**
   * 返回 key 所储存的字符串值的长度
   *
   * @return 字符串值的长度。 当 key 不存在时，返回 0。
   * @author mrys
   */
  public Future<Integer> strLen(String key) {
    return exec(Request.cmd(Command.STRLEN).arg(key), Response::toInteger);
  }

  /**
   * 同时设置一个或多个 key-value 对。
   *
   * @return 总是返回 OK
   * @author mrys
   */
  public Future<String> mSet(Map<String, String> map) {
    Request cmd = Request.cmd(Command.MSET);
    map.forEach((key, value) -> cmd.arg(key).arg(value));
    return exec(cmd, Response::toString);
  }

  /**
   * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
   *
   * @return 当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。
   * @author mrys
   */
  public Future<Integer> mSetNx(Map<String, String> map) {
    Request cmd = Request.cmd(Command.MSETNX);
    map.forEach((key, value) -> cmd.arg(key).arg(value));
    return exec(cmd, Response::toInteger);
  }

  /**
   * 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。
   *
   * @return 设置成功时返回 OK 。
   * @author mrys
   */
  public Future<String> pSetEx(String key, Integer milliseconds, String value) {
    return exec(Request.cmd(Command.PSETEX).arg(key).arg(milliseconds).arg(value),
        Response::toString);
  }


  /**
   * 将 key 中储存的数字值增一。
   *
   * @return 执行 INCR 命令之后 value 的值。
   * @author mrys
   */
  public Future<Integer> incr(String key) {
    return exec(Request.cmd(Command.INCR).arg(key), Response::toInteger);
  }

  /**
   * 将 key 所储存的值加上给定的增量值（increment） 。
   *
   * @return 执行 incrBy 命令之后 value 的值。
   * @author mrys
   */
  public Future<Integer> incrBy(String key, Integer increment) {
    return exec(Request.cmd(Command.INCRBY).arg(key).arg(increment),
        Response::toInteger);
  }

  /**
   * 将 key 所储存的值加上给定的浮点增量值（increment） 。
   *
   * @return 执行 incrByFloat 命令之后 value 的值。
   * @author mrys
   */
  public Future<String> incrByFloat(String key, String increment) {
    return exec(Request.cmd(Command.INCRBYFLOAT).arg(key).arg(increment),
        Response::toString);
  }

  /**
   * 将 key 中储存的数字值减一。
   *
   * @return 执行 decr 命令之后 value 的值。
   * @author mrys
   */
  public Future<Integer> decr(String key) {
    return exec(Request.cmd(Command.DECR).arg(key), Response::toInteger);
  }

  /**
   * key 所储存的值减去给定的减量值（decrement） 。
   *
   * @return 执行 decrBy 命令之后 value 的值。
   * @author mrys
   */
  public Future<Integer> decrBy(String key, Integer increment) {
    return exec(Request.cmd(Command.DECRBY).arg(key).arg(increment),
        Response::toInteger);
  }

  /**
   * Redis Append 命令用于为指定的 key 追加值。
   * <p>
   * 如果 key 已经存在并且是一个字符串， APPEND 命令将 value 追加到 key 原来的值的末尾。
   * <p>
   * 如果 key 不存在， APPEND 就简单地将给定 key 设为 value ，就像执行 SET key value 一样。
   *
   * @return 追加指定值之后， key 中字符串的长度。
   * @author mrys
   */
  public Future<Integer> append(String key, String value) {
    return exec(Request.cmd(Command.APPEND).arg(key).arg(value), Response::toInteger);
  }

  //  <----------------------------string end---------------------------->

  //  <----------------------------hash begin---------------------------->
  //  <----------------------------hash end---------------------------->

  //  <----------------------------List begin---------------------------->

  /**
   * BLPOP key1[ key2 ]timeout 移出并获取列表的第一个元素，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @return 如果列表为空，返回一个 nil 。 否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
   * @author mrys
   */
  public Future<Map<String, String>> bLPoP(Integer timeout, String... keys) {
    Request cmd = Request.cmd(Command.BLPOP);
    for (String key : keys) {
      cmd.arg(key);
    }
    cmd.arg(timeout);
    return exec(cmd, response -> {
      HashMap<String, String> map = new HashMap<>();
      Set<String> responseKeys = response.getKeys();
      for (String key : responseKeys) {
        map.put(key, response.get(key).toString());
      }
      return map;
    });
  }

  /**
   * 2 BRPOP key1[ key2 ]timeout 移出并获取列表的最后一个元素，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   *
   * @return 如果列表为空，返回一个 nil 。 否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
   * @author mrys
   */
  public Future<Map<String, String>> bRPoP(Integer timeout, String... keys) {
    Request cmd = Request.cmd(Command.BRPOP);
    for (String key : keys) {
      cmd.arg(key);
    }
    cmd.arg(timeout);
    return exec(cmd, response -> {
      HashMap<String, String> map = new HashMap<>();
      Set<String> responseKeys = response.getKeys();
      for (String key : responseKeys) {
        map.put(key, response.get(key).toString());
      }
      return map;
    });
  }

  /**
   * 3 BRPOPLPUSH source destination timeout 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它；如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
   * <p>
   * 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素的值，第二个元素是等待时长。
   *
   * @author mrys
   */
  public Future<String> bRPoPLPush(String outKey, String inKey, Integer timeout) {
    return exec(Request.cmd(Command.BRPOPLPUSH).arg(outKey).arg(inKey).arg(timeout),
        Response::toString);
  }


  /**
   * 4 LINDEX key index 通过索引获取列表中的元素
   *
   * @return 列表中下标为指定索引值的元素。 如果指定索引值不在列表的区间范围内，返回 nil 。
   * @author mrys
   */
  public Future<String> lIndex(String key, Integer index) {
    return exec(Request.cmd(Command.LINDEX).arg(key).arg(index), Response::toString);
  }

  /**
   * 5 LINSERT key BEFORE| AFTER pivot value 在列表的元素前或者后插入元素
   *
   * @author mrys
   */
  public Future<String> lInsert(String key, boolean before, String pivot, String value) {
    Request cmd = Request.cmd(Command.LINSERT).arg(key);
    if (before) {
      cmd.arg("BEFORE");
    } else {
      cmd.arg("AFTER ");
    }
    return exec(cmd.arg(pivot).arg(value), Response::toString);
  }

  /**
   * 6 LLEN key 获取列表长度
   *
   * @author mrys
   */
  public Future<Integer> lLen(String key) {
    return exec(Request.cmd(Command.LLEN).arg(key), Response::toInteger);
  }

  /**
   * 7 LPOP key 移出并获取列表的第一个元素
   *
   * @author mrys
   */
  public Future<String> lPop(String key) {
    return exec(Request.cmd(Command.LPOP).arg(key), Response::toString);
  }

  /**
   * 8 LPUSH key value1 [value2] 将一个或多个值插入到列表头部
   *
   * @author mrys
   */
  public Future<Integer> lPush(String key, String... values) {
    Request cmd = Request.cmd(Command.LPUSH).arg(key);
    for (String value : values) {
      cmd.arg(value);
    }
    return exec(cmd, Response::toInteger);
  }

  /**
   * 9 LPUSHX key value 将一个值插入到已存在的列表头部
   *
   * @author mrys
   */
  public Future<Integer> lPushX(String key, String value) {
    return exec(Request.cmd(Command.LPUSHX).arg(key).arg(value), Response::toInteger);
  }

  /**
   * 10 LRANGE key start stop 获取列表指定范围内的元素
   *
   * @author mrys
   */
  public Future<List<String>> lRange(String key, Integer start, Integer end) {
    return exec(Request.cmd(Command.LRANGE).arg(key).arg(start).arg(end),
        response -> response.stream().map(Response::toString).collect(Collectors.toList()));
  }

  /**
   * 11 LREM key count value 移除列表元素
   * <p>
   * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
   * <p>
   * count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
   * <p>
   * count = 0 : 移除表中所有与 VALUE 相等的值。
   *
   * @author mrys
   */
  public Future<Integer> lRem(String key, Integer count, String value) {
    return exec(Request.cmd(Command.LREM).arg(key).arg(count).arg(value), Response::toInteger);
  }

  /**
   * 12 LSET key index value 通过索引设置列表元素的值
   *
   * @return 操作成功返回 ok ，否则返回错误信息。
   * @author mrys
   */
  public Future<String> lSet(String key, Integer index, String value) {
    return exec(Request.cmd(Command.LSET).arg(key).arg(index).arg(value), Response::toString);
  }

  /**
   * 13 LTRIM key start stop
   * <p>
   * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
   * <p>
   * 下标 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推
   *
   * @return ok
   * @author mrys
   */
  public Future<String> lTrim(String key, Integer start, Integer stop) {
    return exec(Request.cmd(Command.LTRIM).arg(key).arg(start).arg(stop), Response::toString);
  }

  /**
   * 14 RPOP key 移除列表的最后一个元素，返回值为移除的元素。
   *
   * @author mrys
   */
  public Future<String> rPop(String key) {
    return exec(Request.cmd(Command.RPOP).arg(key), Response::toString);
  }

  /**
   * 15 RPOPLPUSH source destination 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
   *
   * @author mrys
   */
  public Future<String> rPoPLPush(String outKey, String inKey, Integer timeout) {
    return exec(Request.cmd(Command.RPOPLPUSH).arg(outKey).arg(inKey).arg(timeout),
        Response::toString);
  }

  /**
   * 16 RPUSH key value1 [value2] 在列表中添加一个或多个值
   *
   * @author mrys
   */
  public Future<Integer> rPush(String key, String... values) {
    Request cmd = Request.cmd(Command.RPUSH).arg(key);
    for (String value : values) {
      cmd.arg(value);
    }
    return exec(cmd, Response::toInteger);
  }

  /**
   * 17 RPUSHX key value 为已存在的列表添加值
   *
   * @author mrys
   */
  public Future<Integer> rPushX(String key, String value) {
    return exec(Request.cmd(Command.RPUSHX).arg(key).arg(value), Response::toInteger);
  }

  //  <----------------------------List end---------------------------->

  //  <----------------------------SET begin---------------------------->
  //  <----------------------------set end---------------------------->

  //  <----------------------------pub/sub begin 记得关闭 connection---------------------------->
  public Future<RedisConnection> subscribe(Handler<String> megHandler, String... channels) {
    Promise<RedisConnection> promise = Promise.promise();
    getAccessRedisClient().onSuccess(connection -> {
      Request cmd = Request.cmd(Command.SUBSCRIBE);
      for (String s : channels) {
        cmd.arg(s);
      }
      connection.send(cmd).onSuccess(event1 -> {
        promise.complete(connection);
        connection.handler(event2 -> {
          if (event2.size() == 3) {
            log.info(event2.toString());
          } else {
            megHandler.handle(event2.get(3).toString());
          }
        });
      }).onFailure(promise::fail);
    }).onFailure(promise::fail);
    return promise.future();
  }

  public Future<RedisConnection> pSubscribe(Handler<String> megHandler, String... patterns) {
    Promise<RedisConnection> promise = Promise.promise();
    getAccessRedisClient().onSuccess(connection -> {
      Request cmd = Request.cmd(Command.PSUBSCRIBE);
      for (String s : patterns) {
        cmd.arg(s);
      }
      connection.send(cmd).onSuccess(event1 -> {
        promise.complete(connection);
        connection.handler(event2 -> {
          if (event2.size() == 3) {
            log.info(event2.toString());
          } else {
            megHandler.handle(event2.get(3).toString());
          }
        });
      }).onFailure(promise::fail);
    }).onFailure(promise::fail);
    return promise.future();
  }

  public Future<Response> unSubscribe(String... channels) {
    Request cmd = Request.cmd(Command.UNSUBSCRIBE);
    for (String s : channels) {
      cmd.arg(s);
    }
    return exec(cmd, Function.identity());
  }

  public Future<Response> pUnSubscribe(String... patterns) {
    Request cmd = Request.cmd(Command.PUNSUBSCRIBE);
    for (String s : patterns) {
      cmd.arg(s);
    }
    return exec(cmd, Function.identity());
  }

  public Future<Integer> publish(String channel, String msg) {
    return exec(Request.cmd(Command.PUBLISH).arg(channel).arg(msg), Response::toInteger);
  }

  public Future<Response> pubsub(String... args) {
    Request cmd = Request.cmd(Command.PUBSUB);
    for (String s : args) {
      cmd.arg(s);
    }
    return exec(cmd, Function.identity());
  }

  //  <----------------------------pub/sub end---------------------------->

  //  <----------------------------stream begin---------------------------->
  public void xAdd() {
  }
  //  <----------------------------stream end---------------------------->
}
