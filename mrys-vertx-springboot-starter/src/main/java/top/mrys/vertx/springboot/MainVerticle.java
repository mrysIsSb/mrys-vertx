package top.mrys.vertx.springboot;

import cn.hutool.core.util.StrUtil;
import io.vertx.core.AbstractVerticle;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.consts.ConstLog;
import top.mrys.vertx.springboot.utils.SpringUtils;

/**
 * @author mrys 2021/6/28
 */
@Component("mainVerticle")
@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    log.info(StrUtil.format(ConstLog.log_template1, "hello vertx"));
  }
}
