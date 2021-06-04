package top.mrys.vertx.config.eventbus;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import io.vertx.config.spi.ConfigStore;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.impl.future.PromiseInternal;
import io.vertx.core.json.JsonObject;
import top.mrys.vertx.config.common.StringConstant;

/**
 * @author mrys
 * 2021/5/26
 */
public class MrysEventBusConfigStore implements ConfigStore {

  private final VertxInternal vertx;
  private final MessageConsumer<Object> consumer;
  private final AtomicReference<Buffer> last = new AtomicReference<>();
  private AtomicBoolean inited = new AtomicBoolean(false);
  private String address;

  public MrysEventBusConfigStore(Vertx vertx, String address) {
    this.vertx = (VertxInternal) vertx;
    this.address = address;
    getFrom(vertx, address);

    consumer = vertx.eventBus().consumer(StringConstant.config_update);
    consumer.handler(event -> getFrom(vertx, address));
  }

  private void getFrom(Vertx vertx, String address) {
    vertx.eventBus().request(StringConstant.request_event_bus_address, address,
      event -> {
        if (event.succeeded()) {
          Message<Object> message = event.result();
          getConfig(message);
        }
        inited.set(true);
      });
  }

  private void getConfig(Message<Object> message) {
    Object body = message.body();
    if (body instanceof JsonObject) {
      last.set(((JsonObject) body).toBuffer());
    } else if (body instanceof Buffer) {
      last.set((Buffer) body);
    }
  }

  @Override
  public Future<Void> close() {
    return consumer.unregister();
  }

  @Override
  public Future<Buffer> get() {
    if (inited.get()) {
      Buffer buffer = last.get();
      ContextInternal context = vertx.getOrCreateContext();
      return context.succeededFuture(buffer != null ? buffer : Buffer.buffer("{}"));
    } else {
      PromiseInternal<Buffer> promise = vertx.promise();
      vertx.eventBus().request(StringConstant.request_event_bus_address, address,
        event -> {
          if (event.succeeded()) {
            Message<Object> message = event.result();
            getConfig(message);
            promise.complete(last.get());
          }
          inited.set(true);
        });

      return promise.future();
    }
  }
}
