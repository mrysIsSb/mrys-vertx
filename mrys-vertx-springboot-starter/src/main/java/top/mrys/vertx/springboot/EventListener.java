package top.mrys.vertx.springboot;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import top.mrys.vertx.common.utils.MyJsonUtil;

/**
 * 要用spring.factories 否则bean实例化前的事件监听不到（在用bean的情况）
 * @author mrys
 * @date 2020/10/27
 */
@Slf4j
public class EventListener implements GenericApplicationListener {

//  @org.springframework.context.event.EventListener
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
      config.put("profiles", new JsonObject().put("active", Arrays.asList(profiles)));
      MutablePropertySources propertySources = environment.getPropertySources();
      propertySources.forEach(propertySource -> {
        //命令行参数
        if (SimpleCommandLinePropertySource.class.isAssignableFrom(propertySource.getClass())) {
          String[] propertyNames = ((SimpleCommandLinePropertySource) propertySource)
              .getPropertyNames();
          if (ArrayUtil.isNotEmpty(propertyNames)) {
            JSONObject object = new JSONObject();
            for (String name : propertyNames) {
              object.putByPath(name,propertySource.getProperty(name));
            }
            JsonObject entries = JsonObject.mapFrom(object.toBean(Map.class));
            config.mergeIn(entries, true);
          }
        }
      });
      AtomicBoolean isContinue = new AtomicBoolean(false);

      vertx.deployVerticle(VertxRelevantObjectInstanceFactory::createConfigVerticle,
          new DeploymentOptions().setConfig(config),
          result -> {
            if (result.failed()) {
              log.error(result.result(), result.cause());
            } else {
              VertxPropertySource vertxConfig = VertxPropertySource
                  .getInstance("vertxConfig", VertxRelevantObjectInstanceFactory.getConfigLoader());
              propertySources.addFirst(vertxConfig);
              isContinue.set(true);
            }
          }
      );

      while (!isContinue.get()) {
        try {
          TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }


  @Override
  public boolean supportsEventType(ResolvableType eventType) {
    System.out.println(eventType.getRawClass().getSimpleName());
    return eventType.isAssignableFrom(ApplicationEnvironmentPreparedEvent.class);
  }
}
