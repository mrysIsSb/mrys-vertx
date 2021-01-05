package top.mrys.vertx.common.launcher;

import cn.hutool.core.clone.Cloneable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import top.mrys.vertx.common.config.ConfigLoader;
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
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationContext implements Cloneable<ApplicationContext> {

  private ObjectInstanceFactory instanceFactory = ObjectInstanceFactory.getDefault();

  private ConfigLoader configLoader = new ConfigLoader();

  private MyVerticleFactory verticleFactory = MyVerticleFactory.getDefault();

  /**
   * 克隆当前对象，浅复制
   *
   * @return 克隆后的对象
   */
  @Override
  public ApplicationContext clone() {
    return new ApplicationContext(instanceFactory, configLoader, verticleFactory);
  }

  public <T> T getInstance(Class<T> clazz) {
    return instanceFactory.getInstance(clazz);
  }
}
