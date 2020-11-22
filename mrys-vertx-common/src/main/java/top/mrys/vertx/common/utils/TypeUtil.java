package top.mrys.vertx.common.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;
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

  /**
   * ParameterizedType：参数化类型
   * <p>
   * 参数化类型即我们通常所说的泛型类型，一提到参数，最熟悉的就是定义方法时有形参，然后调用此方法时传递实参。那么参数化类型怎么理解呢？顾名思义，就是将类型由原来的具体的类型参数化，类似于方法中的变量参数，此时类型也定义成参数形式（可以称之为类型形参），然后在使用/调用时传入具体的类型（类型实参）。那么我们的ParameterizedType就是这样一个类型，下面我们来看看它的三个重要的方法：
   * <p>
   * getRawType(): Type 该方法的作用是返回当前的ParameterizedType的类型。如一个List，返回的是List的Type，即返回当前参数化类型本身的Type。
   * <p>
   * getOwnerType(): Type 返回ParameterizedType类型所在的类的Type。如Map.Entry<String,
   * Object>这个参数化类型返回的事Map(因为Map.Entry这个类型所在的类是Map)的类型。 getActualTypeArguments(): Type[]
   * 该方法返回参数化类型<>中的实际参数类型， 如 Map<String,Person>map 这个 ParameterizedType 返回的是 String 类,Person
   * 类的全限定类名的 Type Array。注意: 该方法只返回最外层的<>中的类型，无论该<>内有多少个<>。
   *
   * @author mrys
   */
  public static ParameterizedType getParameterizedType(Type type) {
    return getType(type, ParameterizedType.class);
  }

  /**
   * GenericArrayType：泛型数组类型：
   * <p>
   * 组成数组的元素中有泛型则实现了该接口; 它的组成元素是 ParameterizedType 或 TypeVariable 类型。(通俗来说，就是由参数类型组成的数组。如果仅仅是参数化类型，则不能称为泛型数组，而是参数化类型)。注意：无论从左向右有几个[]并列，这个方法仅仅脱去最右边的[]之后剩下的内容就作为这个方法的返回值。
   * <p>
   * getGenericComponentType(): Type: 返回组成泛型数组的实际参数化类型，如List[] 则返回 List。
   *
   * @author mrys
   */
  public static GenericArrayType getGenericArrayType(Type type) {
    return getType(type, GenericArrayType.class);
  }

  /**
   * TypeVariable：类型变量
   * <p>
   * 范型信息在编译时会被转换为一个特定的类型, 而TypeVariable就是用来反映在JVM编译该泛型前的信息。(通俗的来说，TypeVariable就是我们常用的T，K这种泛型变量)
   * <p>
   * getBounds(): Type[]: 返回当前类型的上边界，如果没有指定上边界，则默认为Object。
   * <p>
   * getName(): String: 返回当前类型的类名
   * <p>
   * getGenericDeclaration(): D 返回当前类型所在的类的Type。
   *
   * @author mrys
   */
  public static TypeVariable getTypeVariable(Type type) {
    return getType(type, TypeVariable.class);
  }

  /**
   * WildcardType: 通配符类型
   * <p>
   * 表示通配符类型，比如 <?>, <? Extends Number>等
   * <p>
   * getLowerBounds(): Type[]: 得到下边界的数组 getUpperBounds(): Type[]: 得到上边界的type数组
   * 注：如果没有指定上边界，则默认为Object，如果没有指定下边界，则默认为String
   *
   * @author mrys
   */
  public static WildcardType getWildcardType(Type type) {
    return getType(type, WildcardType.class);
  }

  public static Class getClass(Type type) {
    return getType(type, Class.class);
  }


  public static Class getRawType(Type type) {
    Class result = (Class) type;
    //参数化类 有泛型的
    if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
      ParameterizedType parameterizedType = getParameterizedType(type);
      result = getClass(parameterizedType.getRawType());
    } else if (GenericArrayType.class.isAssignableFrom(type.getClass())) {
      //表示一种元素类型是参数化类型或者类型变量的数组类型
      GenericArrayType genericArrayType = getGenericArrayType(type);
      result = getClass(genericArrayType.getGenericComponentType());
    } else if (TypeVariable.class.isAssignableFrom(type.getClass())) {
//      TypeVariable typeVariable = getTypeVariable(type);
      result = getClass(Map.class);
    } else if (WildcardType.class.isAssignableFrom(type.getClass())) {
      result = getClass(Map.class);
    }
    return result;
  }
}
