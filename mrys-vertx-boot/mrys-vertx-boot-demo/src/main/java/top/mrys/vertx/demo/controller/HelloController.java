package top.mrys.vertx.demo.controller;

import top.mrys.vertx.http.Path;

/**
 * @author mrys
 * @date 2021/7/6
 */
@Path("/hello")
public class HelloController {

  @Path("/index")
  public void index(Object context) {
    throw new RuntimeException("hello");
  }

}
