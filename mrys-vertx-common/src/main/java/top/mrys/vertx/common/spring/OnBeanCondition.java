package top.mrys.vertx.common.spring;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

/**
 * @author mrys
 * @date 2020/9/17
 */
public class OnBeanCondition implements Condition {

  /**
   * Determine if the condition matches.
   *
   * @param context  the condition context
   * @param metadata the metadata of the {@link AnnotationMetadata class} or {@link MethodMetadata
   *                 method} being checked
   * @return {@code true} if the condition matches and the component can be registered, or {@code
   * false} to veto the annotated component's registration
   */
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    //todo 具体实现
    return false;
  }
}
