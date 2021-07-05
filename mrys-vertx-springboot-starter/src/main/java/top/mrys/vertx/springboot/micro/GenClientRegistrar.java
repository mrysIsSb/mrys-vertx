//package top.mrys.vertx.springboot.micro;
//
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.beans.factory.support.AbstractBeanDefinition;
//import org.springframework.beans.factory.support.BeanDefinitionBuilder;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.ResourceLoaderAware;
//import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.core.type.AnnotationMetadata;
//import org.springframework.core.type.filter.AnnotationTypeFilter;
//import org.springframework.util.Assert;
//import org.springframework.util.ClassUtils;
//import org.springframework.util.StringUtils;
//import top.mrys.vertx.common.factorys.ObjectInstanceFactory;
//import top.mrys.vertx.eventbus.MicroClient;
//
///**
// * 生成请求代理的对象
// *
// * @author mrys
// * @date 2020/11/20
// */
//@Configuration
//public class GenClientRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware,
//    EnvironmentAware {
//
//  private Environment environment;
//
//  private ResourceLoader resourceLoader;
//
//
//  @Override
//  public void setResourceLoader(ResourceLoader resourceLoader) {
//    this.resourceLoader = resourceLoader;
//  }
//
//  @Override
//  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
//      BeanDefinitionRegistry registry) {
///*    BeanDefinitionBuilder definition = BeanDefinitionBuilder
//        .genericBeanDefinition(TTT.class);
//    definition.addPropertyValue("type", TestF.class);
//    definition.addPropertyValue("type1", TestF.class.getName());
//    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//
//
//    AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
//    beanDefinition.setPrimary(true);
//    beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, TestF.class);
//    registry.registerBeanDefinition("Tss", beanDefinition);*/
//    registerMicroClients(importingClassMetadata, registry);
//  }
//
//  public void registerMicroClients(AnnotationMetadata metadata,
//      BeanDefinitionRegistry registry) {
//    //获取bean扫描器
//    ClassPathScanningCandidateComponentProvider scanner = getScanner();
//    scanner.setResourceLoader(this.resourceLoader);
//
//    AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(MicroClient.class);
//    Set<String> basePackages = getBasePackages(metadata);
//
//    scanner.addIncludeFilter(annotationTypeFilter);
//
//    for (String basePackage : basePackages) {
//      Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
//      for (BeanDefinition candidateComponent : candidateComponents) {
//        if (candidateComponent instanceof AnnotatedBeanDefinition) {
//          AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
//          AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
//          Assert.isTrue(annotationMetadata.isInterface(), "@MicroClient 只能用在接口上");
//
//          Map<String, Object> attributes = annotationMetadata
//              .getAnnotationAttributes(MicroClient.class.getCanonicalName());
//          //注册到容器
//          registerMicroClient(registry, annotationMetadata, attributes);
//        }
//      }
//    }
//  }
//
//  private void registerMicroClient(BeanDefinitionRegistry registry,
//      AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
//    String className = annotationMetadata.getClassName();
//    BeanDefinitionBuilder definition = BeanDefinitionBuilder
//        .genericBeanDefinition(MicroClientFactoryBean.class);
//    definition.addPropertyValue("type", className);
//    Class proxyFactoryClass = (Class) attributes.get("proxyFactory");
//    definition.addPropertyValue("proxyFactoryClass", proxyFactoryClass);
//    definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
////    definition.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE); 原型就没法实例化bean
//    AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
//    beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, className);
//    beanDefinition.setPrimary(true);
//    registry.registerBeanDefinition(className, beanDefinition);
//  }
//
//
//  protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
//    Map<String, Object> attributes = importingClassMetadata
//        .getAnnotationAttributes(EnableMicroClient.class.getCanonicalName());
//    Set<String> basePackages = new HashSet<>();
//    for (String pkg : (String[]) attributes.get("value")) {
//      if (StringUtils.hasText(pkg)) {
//        basePackages.add(pkg);
//      }
//    }
//    if (basePackages.isEmpty()) {
//      basePackages.add(
//          ClassUtils.getPackageName(importingClassMetadata.getClassName()));
//    }
//    return basePackages;
//  }
//
//  /**
//   * 获取扫描工具
//   *
//   * @author mrys
//   */
//  protected ClassPathScanningCandidateComponentProvider getScanner() {
//    return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
//      @Override
//      protected boolean isCandidateComponent(
//          AnnotatedBeanDefinition beanDefinition) {
//        boolean isCandidate = false;
//        if (beanDefinition.getMetadata().isIndependent()) {
//          if (!beanDefinition.getMetadata().isAnnotation()) {
//            isCandidate = true;
//          }
//        }
//        return isCandidate;
//      }
//    };
//  }
//
//
//  @Override
//  public void setEnvironment(Environment environment) {
//    this.environment = environment;
//  }
//}
