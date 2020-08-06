package top.mrys.vertx.boot.config;

import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.mysql.annotations.MapperScan;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Configuration
@MapperScan(value = "top.mrys.vertx.boot.dao.mysql")
public class MysqlConfig {

}
