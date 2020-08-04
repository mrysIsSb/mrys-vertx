package top.mrys.vertx.common.utils;

import java.lang.reflect.*;
import lombok.SneakyThrows;

/**
 * @author mrys
 * @date 2020/7/9
 */
public class TypeUtil {

    public static <T extends Type> T getType(Type type, Class<T> toType) {
        if (toType.isAssignableFrom(type.getClass())) {
            return (T) type;
        }
        return null;
    }

    public static ParameterizedType getParameterizedType(Type type) {
        return getType(type, ParameterizedType.class);
    }

    public static GenericArrayType getGenericArrayType(Type type) {
        return getType(type, GenericArrayType.class);
    }

    public static TypeVariable getTypeVariable(Type type) {
        return getType(type, TypeVariable.class);
    }

    public static WildcardType getWildcardType(Type type) {
        return getType(type, WildcardType.class);
    }

    public static Class getClass(Type type) {
        return getType(type, Class.class);
    }
}
