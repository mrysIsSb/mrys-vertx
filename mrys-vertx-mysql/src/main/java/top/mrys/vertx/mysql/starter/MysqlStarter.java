package top.mrys.vertx.mysql.starter;

import io.vertx.core.json.JsonObject;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.StringUtils;
import top.mrys.vertx.common.launcher.AbstractStarter;
import top.mrys.vertx.common.launcher.MyLauncher;
import top.mrys.vertx.common.launcher.MyRefreshableApplicationContext;
import top.mrys.vertx.common.utils.ScanPackageUtil;
import top.mrys.vertx.mysql.annotations.MapperScan;
import top.mrys.vertx.mysql.mybatis.MyBatisConfiguration;
import top.mrys.vertx.mysql.mybatis.MySqlSessionFactory;

/**
 * @author mrys
 * @date 2020/8/5
 */
public class MysqlStarter extends AbstractStarter<EnableMysql> {


  @Autowired
  private MyRefreshableApplicationContext context;


  @Override
  public void start(EnableMysql enableMysql) {
//    ClassPathBeanDefinitionScanner
    MyBatisConfiguration configuration = new MyBatisConfiguration();
    MySqlSessionFactory mySqlSessionFactory = new MySqlSessionFactory(configuration);
    try {
      Map<String, Object> beansWithAnnotation = context
          .getBeansWithAnnotation(MapperScan.class);

      beansWithAnnotation.values().forEach(o -> {
        Set<MapperScan> mapperScans = AnnotatedElementUtils
            .findAllMergedAnnotations(o.getClass(), MapperScan.class);
        mapperScans.forEach(mapperScan -> {
          String value = mapperScan.value();
          Class[] classes = ScanPackageUtil.getClassFromPackage(value);
          for (Class aClass : classes) {
            if (aClass.isInterface()) {
              configuration.addMapper(aClass);
              Object o1 = Proxy
                  .newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{aClass},
                      (proxy, method, args) -> {
                        Object mapper = mySqlSessionFactory.openSession().getMapper(aClass);
                        return method.invoke(mapper, args);
                      });
              context.registerBean(aClass, () -> o1);
            }
          }
        });
      });
    } catch (BeansException e) {

    }

  }
}
