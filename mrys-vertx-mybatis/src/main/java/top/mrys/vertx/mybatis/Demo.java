package top.mrys.vertx.mybatis;

import java.util.Collections;
import java.util.Map;

import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.SqlTemplate;

/**
 * @author mrys
 * 2021/6/21
 */
public class Demo {
  public static void main(String[] args) {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
            .setPort(3306)
            .setHost("the-host")
            .setDatabase("the-db")
            .setUser("user")
            .setPassword("secret");

// Pool options
    PoolOptions poolOptions = new PoolOptions()
            .setMaxSize(5);

// Create the client pool
    MySQLPool client = MySQLPool.pool(connectOptions, poolOptions);

// A simple query
    client
            .query("SELECT * FROM users WHERE id='julien'")
            .execute(ar -> {
              if (ar.succeeded()) {
                RowSet<Row> result = ar.result();
                System.out.println("Got " + result.size() + " rows ");
              } else {
                System.out.println("Failure: " + ar.cause().getMessage());
              }

              // Now close the pool
              client.close();
            });

    Map<String, Object> parameters = Collections.singletonMap("id", 1);

    SqlTemplate
            .forQuery(client, "SELECT * FROM users WHERE id=#{id}")
            .execute(parameters)
            .onSuccess(users -> {
              users.forEach(row -> {
                System.out.println(row.getString("first_name") + " " + row.getString("last_name"));
              });
            });
  }
}
