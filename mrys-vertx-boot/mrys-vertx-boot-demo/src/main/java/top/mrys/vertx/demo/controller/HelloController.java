package top.mrys.vertx.demo.controller;

import io.vertx.core.buffer.Buffer;
import top.mrys.vertx.http.HttpContext;
import top.mrys.vertx.http.Path;

/**
 * @author mrys
 * @date 2021/7/6
 */
@Path("/hello")
public class HelloController {

  @Path("/index")
  public void index(HttpContext context) {
    context.write(Buffer.buffer("hello"));
  }

}
