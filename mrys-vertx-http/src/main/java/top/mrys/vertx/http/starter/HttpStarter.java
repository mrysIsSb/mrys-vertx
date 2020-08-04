package top.mrys.vertx.http.starter;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import top.mrys.vertx.common.launcher.AbstractStarter;
import top.mrys.vertx.common.launcher.Starter;
import top.mrys.vertx.common.launcher.VertxStartedEvent;
import top.mrys.vertx.common.utils.ScanPackageUtil;
import top.mrys.vertx.http.parser.RouteFactory;

/**
 * @author mrys
 * @date 2020/8/4
 */
@Slf4j
@ComponentScan("top.mrys.vertx.http.parser")
public class HttpStarter extends AbstractStarter<EnableHttp> {

  @Autowired(required = false)
  private Vertx vertx;

  @Override
  public void start(EnableHttp enableHttp) {
    int port = enableHttp.port();
    if (Objects.isNull(vertx)) {
      log.error("vertx 不能为空null");
      return;
    }
    String[] packages = enableHttp.scanPackage();
    Assert.notEmpty(packages, "包不能为空");
    ArrayList<Class> classes = new ArrayList<>();
    for (String s : packages) {
      Class[] classFromPackage = ScanPackageUtil.getClassFromPackage(s);
      classes.addAll(Arrays.asList(classFromPackage));
    }
    RouteFactory factory = RouteFactory.create(vertx, classes);
    Router router = factory.get();
    vertx.createHttpServer().requestHandler(router)
        .listen(port)
        .onSuccess(event -> log.info("http start :{}", event.actualPort()))
        .onFailure(event -> log.error(event.getMessage(), event));
  }
}
