package top.mrys.vertx.common.utils;

import cn.hutool.core.util.ClassUtil;

/**
 * 包扫描工具
 *
 * @author mrys
 * @date 2020/7/21
 */
public class ScanPackageUtil {

  public Class[] getClassFromPackage(String packageName) {
    return ClassUtil.scanPackage(packageName, c -> true).toArray(new Class[]{});
  }
}
