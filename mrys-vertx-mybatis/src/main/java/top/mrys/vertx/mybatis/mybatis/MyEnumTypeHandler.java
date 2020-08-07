package top.mrys.vertx.mybatis.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * @author mrys
 * @date 2020/8/5
 */
public class MyEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

  private Class<E> eClass;

  public MyEnumTypeHandler(Class<E> eClass) {
    this.eClass = eClass;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
      throws SQLException {
    if (parameter instanceof EnumType) {
      Object key = ((EnumType) parameter).getKey();
      ps.setObject(i, key);
    } else {
      ps.setObject(i, parameter.ordinal());
    }
  }

  private E getE(Object o) {
    if (Objects.isNull(o)) {
      return null;
    }
    E[] es = eClass.getEnumConstants();
    if (EnumType.class.isAssignableFrom(eClass)) {
      for (E e : es) {
        EnumType type = (EnumType) e;
        if (o.equals(type.getKey())) {
          return e;
        }
      }
      return null;
    }else {
      return es[(int) o];
    }
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Object object = rs.getObject(columnName);
    return getE(object);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return getE(rs.getObject(columnIndex));
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return getE(cs.getObject(columnIndex));
  }
}
