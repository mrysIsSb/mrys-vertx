package top.mrys.vertx.boot.config;

import org.springframework.context.annotation.Configuration;
import top.mrys.vertx.mysql.annotations.MapperScan;
import top.mrys.vertx.mysql.starter.EnableMysql;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Configuration
@MapperScan(value = "top.mrys.vertx.top.mrys.vertx.boot.dao.mysql")
@EnableMysql
public class MysqlConfig {

}
