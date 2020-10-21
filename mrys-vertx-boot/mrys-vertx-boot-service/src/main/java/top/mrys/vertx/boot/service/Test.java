package top.mrys.vertx.boot.service;

import cn.hutool.core.lang.Filter;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import java.lang.reflect.Method;
import top.mrys.vertx.boot.entity.SysUser;
import top.mrys.vertx.common.utils.AnnotationUtil;

/**
 * @author mrys
 * @date 2020/10/21
 */
public class Test {

  public static void main(String[] args) throws NoSuchMethodException {
    Method getAll = SysUserServiceImpl.class.getMethod("getAll", SysUser.class);
    System.out.println(AnnotationUtil.getInterfaceMethodParameterAnnotation(getAll.getParameters()[0],0));
  }
}
