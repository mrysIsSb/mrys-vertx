package top.mrys.vertx.common.launcher;

import org.springframework.context.ApplicationEvent;

/**
 * @author mrys
 * @date 2020/8/4
 */
public class VertxStartedEvent extends ApplicationEvent {


  public VertxStartedEvent(Object source) {
    super(source);
  }
}
