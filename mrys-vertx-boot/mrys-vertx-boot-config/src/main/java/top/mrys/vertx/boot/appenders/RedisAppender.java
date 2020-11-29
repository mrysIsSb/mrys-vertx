package top.mrys.vertx.boot.appenders;

import java.io.Serializable;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * @author mrys
 * @date 2020/11/28
 */
@Plugin(name = "redis", category = "Core", elementType = "appender", printObject = true)
public class RedisAppender extends AbstractAppender {

  protected RedisAppender(String name, Filter filter,
      Layout<? extends Serializable> layout,
      boolean ignoreExceptions, Property[] properties) {
    super(name, filter, layout, ignoreExceptions, properties);
  }

  @Override
  public void append(LogEvent logEvent) {
    if (logEvent.getMarker() != null) {
      System.out.println(logEvent.getMarker().getName()+"--"+logEvent.getMessage().getFormattedMessage());
    }
  }

  @PluginFactory
  public static RedisAppender createAppender(@PluginAttribute("name") String name,
      @PluginElement("Filter") final Filter filter,
      @PluginElement("Layout") Layout<? extends Serializable> layout,
      @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
    if (name == null) {
      LOGGER.error("No name provided for MyCustomAppenderImpl");
      return null;
    }
    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }
    return new RedisAppender(name, filter, layout, ignoreExceptions,null);
  }

}
