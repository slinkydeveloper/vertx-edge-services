package io.slinkydeveloper.brewery.order.service;

import io.slinkydeveloper.brewery.order.api.Order;
import io.slinkydeveloper.brewery.order.api.OrderService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;
import java.util.Map;

public class OrderServiceImpl implements OrderService {

  Map<Long, Order> orders;

  public OrderServiceImpl(Vertx vertx) {
    this.orders = vertx.sharedData().getLocalMap("orders");
  }

  @Override
  public void getOrder(long id, Handler<AsyncResult<Order>> resultHandler) {
    resultHandler.handle(Future.succeededFuture(orders.get(id)));
  }

  @Override
  public void placeANewOrder(long customerId, List<Long> beersId, float total, Handler<AsyncResult<Order>> resultHandler) {
    Order o = new Order(System.currentTimeMillis(), customerId, beersId, total);
    orders.put(o.getId(), o);
    resultHandler.handle(Future.succeededFuture(o));
  }
}
