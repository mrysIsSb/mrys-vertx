import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisConnection;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import top.mrys.vertx.redis.RedisTemplate;

/**
 * @author mrys
 * @date 2020/11/26
 */
@Slf4j
@RunWith(VertxUnitRunner.class)
public class RedisTest {
//  TestSuite suite = TestSuite.create("the_test_suite");

  @Rule
  public RunTestOnContext rule = new RunTestOnContext();

  private Redis client;
  private RedisTemplate template;
  private Vertx vertx;

  @Before
  public void before() {
    System.out.println("连接redis");
    vertx = rule.vertx();
//    client = Redis.createClient(rule.vertx(), "redis://123456@192.168.1.2:6379/1");
    client = Redis.createClient(rule.vertx(), "redis://192.168.1.6:6379");
    template = new RedisTemplate(client, "123456", 5);
    template.setAutoClose(false);
  }

  @After
  public void tearDown(TestContext ctx) {
    vertx = rule.vertx();
    vertx.close(ctx.asyncAssertSuccess());
  }

  @Test
  public void testP(TestContext context) {
    template.set("incr1", "1")
        .compose(s -> {
          System.out.println(s);
          return template.get("incr1");
        })
        .compose(s -> {
          System.out.println(s);
          return template.keys("*");
        })
        .onComplete(context.asyncAssertSuccess());
  }

  @Test
  public void testSet(TestContext context) {
    template.set("incr1", "1")
        .onComplete(context
            .asyncAssertSuccess(event -> Assert.assertEquals("OK", event)));
/*    template.set("hello", "mrys")
        .onSuccess(event -> context.assertEquals("OK", event))
        .onFailure(context::fail);*/
  }

  @Test
  public void testGet(TestContext context) {
    template.get("hello")
        .onSuccess(System.out::println)
        .onComplete(context.asyncAssertSuccess(event -> Assert.assertEquals("mrys", event)));
  }

  @Test
  public void testKeys(TestContext context) {
    template.keys("*")
        .onSuccess(System.out::println)
        .onComplete(context.asyncAssertSuccess());
  }

  @Test
  public void testScan(TestContext context) {
    template.scan(1, "*", 1)
        .onSuccess(event -> event
            .forEach((key, value) -> System.out.println(key + "---" + Arrays.toString(value))))
        .onComplete(context.asyncAssertSuccess());
  }

  @Test
  public void testIncr(TestContext context) {
    template.decrBy("incr1", 12)
        .onSuccess(System.out::println)
        .onComplete(context.asyncAssertSuccess());
  }

  public static void testPSubscribe(Vertx vertx, RedisTemplate template) {

    vertx.deployVerticle(new AbstractVerticle() {
      private RedisConnection connection;

      @Override
      public void start() throws Exception {
        template
            .pSubscribe(event -> System.out.println("msg:" + event), "c1*", "c2")
            .onSuccess(event -> connection = event);
      }

      @Override
      public void stop() throws Exception {
        if (connection != null) {
          connection.close();
        }
        System.out.println("stop");
      }
    });
  }

  public static void testSubscribe(Vertx vertx, RedisTemplate template) {

    vertx.deployVerticle(new AbstractVerticle() {
      private RedisConnection connection;

      @Override
      public void start() throws Exception {
        template
            .subscribe(event -> System.out.println("msg:" + event), "__keyevent@5__:del","c1")
            .onSuccess(event -> connection = event);
      }

      @Override
      public void stop() throws Exception {
        if (connection != null) {
          connection.close();
        }
        System.out.println("stop");
      }
    });
  }

  @Test
  public void testPublish(TestContext context) throws InterruptedException {
    vertx.setPeriodic(2000, event -> template.publish("c1", "你好").onSuccess(System.out::println));
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    RedisTemplate template = new RedisTemplate(
        Redis.createClient(vertx, "redis://123456@192.168.1.6:6379/5"));
    template.setAutoClose(false);
//    testPSubscribe(vertx, template);
    testSubscribe(vertx, template);
   /* template.keys("*")
        .onSuccess(System.out::println);
    template.publish("c1", "你好").onSuccess(System.out::println);
    vertx.setPeriodic(2000, event -> template.publish("c1", "你好").onSuccess(System.out::println));*/
  }


}
