package io.slinkydeveloper.brewery.order.service;

import io.slinkydeveloper.brewery.order.api.OrderService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import io.vertx.serviceproxy.ServiceBinder;

public class MainVerticle extends AbstractVerticle {

  private MessageConsumer<JsonObject> messageConsumer;

  @Override
  public void start(Future<Void> fut) {
    messageConsumer = new ServiceBinder(vertx)
      .setAddress("orders.myapplication")
      .register(OrderService.class, new OrderServiceImpl(vertx));

    TcpEventBusBridge bridge = TcpEventBusBridge.create(
      vertx,
      new BridgeOptions()
        .addInboundPermitted(new PermittedOptions().setAddress("orders.myapplication"))
        .addOutboundPermitted(new PermittedOptions().setAddress("orders.myapplication"))
    );
    bridge.listen(9002, t -> fut.complete());
  }

  @Override
  public void stop(Future<Void> fut) throws Exception {
    messageConsumer.unregister(h -> fut.complete());
  }
}
