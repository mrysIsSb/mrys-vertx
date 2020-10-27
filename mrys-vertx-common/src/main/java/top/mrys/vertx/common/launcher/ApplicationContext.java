package top.mrys.vertx.common.launcher;

import lombok.Getter;
import lombok.Setter;
import top.mrys.vertx.common.factorys.DefaultObjectInstanceFactory;
import top.mrys.vertx.common.factorys.ObjectInstanceFactory;

/**
 * 程序环境
 *
 * @author mrys
 * @date 2020/10/27
 */
@Setter
@Getter
public class ApplicationContext {

  private ObjectInstanceFactory instanceFactory = new DefaultObjectInstanceFactory();

}
