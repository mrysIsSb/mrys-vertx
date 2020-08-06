package top.mrys.vertx.boot.enums;

import lombok.Getter;
import top.mrys.vertx.mysql.mybatis.EnumType;

/**
 * @author mrys
 * @date 2020/8/5
 */
@Getter
public enum EnumSex implements EnumType<Integer> {
  un_know(0, "未知"),
  man(1,"男"),
  woman(2, "女"),
  ;
  private final int code;
  private final String value;

  EnumSex(int code, String value) {
    this.code = code;
    this.value = value;
  }

  @Override
  public Integer getKey() {
    return code;
  }
}
