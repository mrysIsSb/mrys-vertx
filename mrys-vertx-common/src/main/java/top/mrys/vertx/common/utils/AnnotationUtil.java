package top.mrys.vertx.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author mrys
 * @date 2020/7/4
 */
public class AnnotationUtil {

    public static <A  extends Annotation> Method[] getMethodByAnnotation(Class clazz, Class<A> aClass) {
        Method[] methods = clazz.getMethods();
        return Arrays.stream(methods)
                .filter(method -> isHaveAnyAnnotations(method.getAnnotations(),aClass))
                .toArray(Method[]::new);
    }

    /**
     * 是否存在任意的注解
     * @author mrys
     */
    public static <A  extends Annotation> Boolean isHaveAnyAnnotations(AnnotatedElement annotatedElement, Class<A>... annotationClass) {
        Annotation[] annotations = annotatedElement.getAnnotations();
        return isHaveAnyAnnotations(annotations, annotationClass);
    }

    public static <A  extends Annotation> Boolean isHaveAnyAnnotations(Annotation[] as, Class<A>... annotationClass) {
        for (Annotation annotation : as) {
            for (Class<A> aClass : annotationClass) {
                if (annotation.annotationType().equals(aClass)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <A extends Annotation> A getAnnotation(AnnotatedElement annotatedElement,
        Class<A> aClass) {
        if (isHaveAnyAnnotations(annotatedElement, aClass)) {
            return annotatedElement.getAnnotation(aClass);
        }
        return null;
    }
}
