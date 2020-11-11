package top.mrys.vertx.springboot.http.server;

import lombok.extern.slf4j.Slf4j;
import top.mrys.vertx.http.parser.DefaultRouteFactory;
import top.mrys.vertx.http.parser.RouteFactory;

/**
 * @author mrys
 * @date 2020/8/7
 */
@Slf4j
public class SpringRouteFactoryWarp extends DefaultRouteFactory {


 /* @Autowired
  private Vertx vertx;

  @Autowired
  private List<AbstractHandlerParser> abstractHandlerParsers;


  @PostConstruct
  public void init() throws Exception {
    super.vertx=vertx;
    classes = context.getBeansWithAnnotation(RouteHandler.class).values().stream().map(Object::getClass)
        .distinct().collect(
        Collectors.toList());
    interceptors.add(new HttpInterceptor());
    parsers.addAll(abstractHandlerParsers);
  }*/

}
