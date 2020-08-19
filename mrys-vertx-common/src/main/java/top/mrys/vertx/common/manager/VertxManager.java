package top.mrys.vertx.common.manager;

import cn.hutool.core.collection.CollectionUtil;
import io.vertx.core.Vertx;
import io.vertx.core.impl.Deployment;
import io.vertx.core.impl.VertxImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.mrys.vertx.common.exceptions.MrysException;
import top.mrys.vertx.common.exceptions.VertxEmptyException;

/**
 * @author mrys
 * @date 2020/8/19
 */
@Component
public class VertxManager {

  @Autowired
  private Vertx vertx;

  private void checkVertx() {
    if (Objects.isNull(vertx)) {
      VertxEmptyException.doThrow();
    }
  }

  public List<Deployment> getDeployment() {
    checkVertx();
    Set<String> ids = vertx.deploymentIDs();
    List<Deployment> deployments = new ArrayList<>();
    if (CollectionUtil.isNotEmpty(ids)) {
      VertxImpl vertx = (VertxImpl) this.vertx;
       deployments = ids.stream().map(vertx::getDeployment)
          .collect(Collectors.toList());
    }
    return deployments;
  }
}
