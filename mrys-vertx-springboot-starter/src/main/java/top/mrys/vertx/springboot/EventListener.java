package top.mrys.vertx.springboot;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.impl.JsonUtil;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

/**
 * @author mrys
 * @date 2020/10/27
 */
@Slf4j
public class EventListener implements GenericApplicationListener {

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ApplicationEnvironmentPreparedEvent) {
      ApplicationEnvironmentPreparedEvent preparedEvent = (ApplicationEnvironmentPreparedEvent) event;
      ConfigurableEnvironment environment = preparedEvent.getEnvironment();
      SpringApplication springApplication = preparedEvent.getSpringApplication();
      springApplication.setWebApplicationType(WebApplicationType.NONE);
      String[] profiles = environment.getActiveProfiles();
      Vertx vertx = VertxRelevantObjectInstanceFactory.createVertx();
      JsonObject config = new JsonObject();
      //传入指定的配置属性
      config.put("profiles", Arrays.asList(profiles));
      AtomicBoolean isContinue = new AtomicBoolean(false);
      vertx.eventBus().consumer("config.data", event1 -> {
        JsonObject body = (JsonObject) event1.body();
        environment.getPropertySources()
            .addFirst(new JsonPropertySource("vertx Config", body));
        isContinue.set(true);
      });
      vertx.deployVerticle(VertxRelevantObjectInstanceFactory::createConfigVerticle,
          new DeploymentOptions().setConfig(config),
          result -> {
            if (result.failed()) {
             log.error(result.result(),result.cause());
            }
          }
      );
      while (!isContinue.get()) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(isContinue.get());
      }
    }
  }


  @Override
  public boolean supportsEventType(ResolvableType eventType) {
    System.out.println(eventType.getRawClass().getSimpleName());
    return eventType.isAssignableFrom(ApplicationEnvironmentPreparedEvent.class);
  }
}
