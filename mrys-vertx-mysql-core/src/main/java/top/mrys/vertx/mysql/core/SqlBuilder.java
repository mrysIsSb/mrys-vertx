package top.mrys.vertx.mysql.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author mrys
 * 2021/6/21
 */
public class SqlBuilder {

  public static InsertSqlBuilder insert() {
    return new InsertSqlBuilder();
  }

  public static DelSqlBuilder delete() {
    return new DelSqlBuilder();
  }

  public static SelectSqlBuilder select() {
    return new SelectSqlBuilder();
  }

  public static UpdateSqlBuilder update() {
    return new UpdateSqlBuilder();
  }

  static class InsertSqlBuilder implements SqlFieldBuilder<InsertSqlBuilder> {
    String p;
    String table;
    List<SqlField> fields = new ArrayList<>();
    List<String[]> values = new ArrayList<>();

    public InsertSqlBuilder() {
      this("INSERT INTO ");
    }

    public InsertSqlBuilder(String p) {
      this.p = p;
    }

    public InsertSqlStep1 table(String table) {
      this.table = table;
      return new InsertSqlStep1(this);
    }

    public InsertSqlBuilder addValues(String... values) {
      this.values.add(values);
      return this;
    }

    @Override
    public InsertSqlBuilder addField(SqlField field) {
      this.fields.add(field);
      return this;
    }

    public String build() {
      StringBuilder builder = new StringBuilder(p);
      builder.append("`").append(table).append("` (");
      fields.forEach(field -> builder.append(field.toString()).append(","));
      builder.delete(builder.length()-1,builder.length());
      builder.append(")").append(" VALUES ");
      values.forEach(vs -> {
        builder.append("(")
                .append(String.join(",",vs))
                .append("),");
      });
      builder.delete(builder.length()-1,builder.length());
      return builder.toString();
    }

    static class InsertSqlStep1 {
      private InsertSqlBuilder builder;

      public InsertSqlStep1(InsertSqlBuilder builder) {
        this.builder = builder;
      }

      public InsertSqlStep1 addField(SqlField field) {
        builder.addField(field);
        return this;
      }

      public InsertSqlStep2 values() {
        return new InsertSqlStep2(builder);
      }
    }

    static class InsertSqlStep2 {
      private InsertSqlBuilder builder;

      public InsertSqlStep2(InsertSqlBuilder builder) {
        this.builder = builder;
      }

      public InsertSqlStep2 addValues(String... value) {
        builder.addValues(value);
        return this;
      }

      public String build() {
        return builder.build();
      }
    }
  }

  static class DelSqlBuilder {

  }

  /**
   * select * from test where id = 1
   *
   * @author mrys
   */
  static class SelectSqlBuilder implements SqlFieldBuilder<SelectSqlBuilder> {
    String p;
    Set<SqlField> fields = new HashSet<>();
    String table;

    public SelectSqlBuilder() {
      this("SELECT ");
    }

    public SelectSqlBuilder(String p) {
      this.p = p;
    }

    @Override
    public SelectSqlBuilder addField(SqlField field) {
      this.fields.add(field);
      return this;
    }

    public SelectSqlBuilder from(String table) {
      this.table = table + " ";
      return this;
    }

    public SelectSqlBuilder where() {
      return this;
    }
  }

  static class UpdateSqlBuilder {

  }

  static class SqlField {
    String fieldName;

    public SqlField(String fieldName) {
      this.fieldName = fieldName;
    }

    public static SqlField byName(String fieldName) {
      return new SqlField(fieldName);
    }

    public String toString() {
      return "`"+fieldName+"` ";
    }
  }

  interface SqlFieldBuilder<B> {
    B addField(SqlField field);

    default B addFields(String... names) {
      for (String name : names) {
        addField(SqlField.byName(name));
      }
      return (B) this;
    }
  }

  public static void main(String[] args) {
    String s = SqlBuilder.insert()
            .table("hello")
            .addField(SqlField.byName("h"))
            .addField(SqlField.byName("h212"))
            .values()
            .addValues("hello", "sadf")
            .addValues("hello", "sadf")
            .build();
    System.out.println(s);
  }
}
