package io.slinkydeveloper.brewery.order.api;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@VertxGen
@ProxyGen
public interface OrderService {

  void getOrder(long id, Handler<AsyncResult<Order>> resultHandler);
  void placeANewOrder(long customerId, List<Long> beersId, float total, Handler<AsyncResult<Order>> resultHandler);

  static OrderService createProxy(Vertx vertx, String address) {
    return new OrderServiceVertxEBProxy(vertx, address);
  }

}
