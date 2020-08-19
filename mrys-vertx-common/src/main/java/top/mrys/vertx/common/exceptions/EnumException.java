package top.mrys.vertx.common.exceptions;

import lombok.Getter;

/**
 * @author mrys
 * @date 2020/8/19
 */
public enum EnumException {
  EMPTY_VERTX("vertx为空!");

  @Getter
  private final String msg;

  EnumException(String msg) {
    this.msg = msg;
  }
}
