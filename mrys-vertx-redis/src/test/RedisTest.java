import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.redis.client.Redis;
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
    client = Redis.createClient(rule.vertx(), "redis://123456@192.168.124.44:6379/1");
//    client = Redis.createClient(rule.vertx(),"redis://192.168.124.44:6379");
    template = new RedisTemplate(client);
  }

  @After
  public void tearDown(TestContext ctx) {
    vertx = rule.vertx();
    vertx.close(ctx.asyncAssertSuccess());
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


}
