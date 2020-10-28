package top.mrys.vertx.springboot;

import java.util.Arrays;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * @author mrys
 * @date 2020/10/27
 */
public class EventListener implements GenericApplicationListener {

  @Override
  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ApplicationEnvironmentPreparedEvent) {
      ApplicationEnvironmentPreparedEvent preparedEvent = (ApplicationEnvironmentPreparedEvent) event;
      ConfigurableEnvironment environment = preparedEvent.getEnvironment();
      SpringApplication springApplication = preparedEvent.getSpringApplication();
      springApplication.setWebApplicationType(WebApplicationType.NONE);
      environment.getPropertySources().forEach(propertySource -> {
        System.out.println(propertySource.getName());
        System.out.println(propertySource.getSource());
        System.out.println(propertySource.getClass());
      });
    }
  }


  @Override
  public boolean supportsEventType(ResolvableType eventType) {
    System.out.println(eventType.getRawClass().getSimpleName());
    return eventType.isAssignableFrom(ApplicationEnvironmentPreparedEvent.class);
  }
}
