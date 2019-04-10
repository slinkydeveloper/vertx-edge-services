package io.slinkydeveloper.brewery.order.service;

import io.slinkydeveloper.brewery.order.api.OrderService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.serviceproxy.ServiceBinder;

public class MainVerticle extends AbstractVerticle {

  private MessageConsumer<JsonObject> messageConsumer;

  @Override
  public void start(Future<Void> fut) {
    messageConsumer = new ServiceBinder(vertx)
      .setAddress("orders.myapplication")
      .register(OrderService.class, new OrderServiceImpl(vertx));
  }

  @Override
  public void stop(Future<Void> fut) throws Exception {
    messageConsumer.unregister(h -> fut.complete());
  }
}
