package top.mrys.vertx.http.starter;

import cn.hutool.core.util.ArrayUtil;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import top.mrys.vertx.common.launcher.Starter;
import top.mrys.vertx.common.utils.ScanPackageUtil;
import top.mrys.vertx.common.utils.VertxHolder;
import top.mrys.vertx.http.parser.RouteFactory;

/**
 * @author mrys
 * @date 2020/8/4
 */
@Slf4j
public class HttpStarter implements Starter<EnableHttp> {

  @Override
  public void start(EnableHttp enableHttp) {
    int port = enableHttp.port();
    Vertx mainVertx = VertxHolder.getMainVertx();
    Assert.notNull(mainVertx, "vertx 不能为空null");
    String[] packages = enableHttp.scanPackage();
    Assert.notEmpty(packages,"包不能为空");
    ArrayList<Class> classes = new ArrayList<>();
    for (String s : packages) {
      Class[] classFromPackage = ScanPackageUtil.getClassFromPackage(s);
      classes.addAll(Arrays.asList(classFromPackage));
    }
    RouteFactory factory = RouteFactory.create(mainVertx, classes);
    Router router = factory.get();
    mainVertx.createHttpServer().requestHandler(router)
        .listen(port)
        .onSuccess(event -> log.info("http start :{}",event.actualPort()))
    .onFailure(event -> log.error(event.getMessage(),event));
  }
}
