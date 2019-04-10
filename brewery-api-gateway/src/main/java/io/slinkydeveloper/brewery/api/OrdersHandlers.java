package io.slinkydeveloper.brewery.api;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.slinkydeveloper.brewery.api.models.ApiOrder;
import io.slinkydeveloper.brewery.beers.reactivex.client.BeersApiClient;
import io.slinkydeveloper.brewery.order.api.Order;
import io.slinkydeveloper.brewery.order.reactivex.api.OrderService;
import io.slinkydeveloper.brewery.styles.StylesServiceGrpc;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersHandlers {

  private BeersApiClient beersServiceClient;
  private StylesServiceGrpc.StylesServiceVertxStub stylesServiceClient;
  private WebClient customersServiceClient;
  private OrderService orderServiceProxy;
  private BeersHandlers beersHandlers;

  public OrdersHandlers(BeersApiClient beersServiceClient, StylesServiceGrpc.StylesServiceVertxStub stylesServiceClient, WebClient customersServiceClient, OrderService orderServiceProxy, BeersHandlers beersHandlers) {
    this.beersServiceClient = beersServiceClient;
    this.stylesServiceClient = stylesServiceClient;
    this.customersServiceClient = customersServiceClient;
    this.orderServiceProxy = orderServiceProxy;
    this.beersHandlers = beersHandlers;
  }

  public void handleGetOrder(RoutingContext routingContext) {
    this
      .orderServiceProxy
      .rxGetOrder(Long.parseLong(routingContext.pathParam("id")))
      .flatMap(this::fillOrder)
      .subscribe(
        result ->
          routingContext
            .response()
            .setStatusCode(200)
            .putHeader("content-type", "application/json")
            .end(result.toJson().encode()),
        routingContext::fail
      );
  }

  public Single<ApiOrder> fillOrder(Order order) {
    return customersServiceClient
      .post("/graphql")
      .rxSendBuffer(Buffer.buffer("{\"query\":\"query {customer(id: \\\"" + order.getCustomerId() + "\\\"){id,name}}\\n\"}"))
      .map(HttpResponse::bodyAsJsonObject)
      .map(jo -> jo.getJsonObject("data").getJsonObject("customer"))
      .map(jo -> order.toJson().put("customerName", jo.getString("name")))
      .flatMap(jo ->
        Observable
          .fromIterable(jo.getJsonArray("beersId"))
          .flatMapSingle(o -> beersServiceClient.rxGetBeer(((Long)o).toString()))
          .map(r -> new io.slinkydeveloper.brewery.beers.client.models.Beer(r.bodyAsJsonObject()))
          .flatMapSingle(beersHandlers::solveStyleAndBuildApiBeer)
          .collectInto(new JsonArray(), (j, apiBeer) -> j.add(apiBeer.toJson()))
          .map(beersArray -> jo.put("beers", beersArray))
          .map(ApiOrder::new)
      );
  }


  public void handlePostOrder(RoutingContext routingContext) {
    JsonObject body = routingContext.getBodyAsJson();
    Long customerId = body.getLong("customerId");
    List<Long> beersId = body.getJsonArray("beersId").stream().map(o -> (Long)o).collect(Collectors.toList());
    Single<JsonObject> customerSingle = customersServiceClient
      .post("/graphql")
      .rxSendBuffer(Buffer.buffer("{\"query\":\"query {customer(id: \\\"" + customerId + "\\\"){id,name}}\\n\"}"))
      .flatMap(response -> {
        if (response.statusCode() == 404) {
          return Single.error(new IllegalArgumentException("Customer not found"));
        } else {
          return Single.just(response.bodyAsJsonObject().getJsonObject("data").getJsonObject("customer"));
        }
      });
    Single<Float> total =
      Observable.fromIterable(beersId)
        .flatMapSingle(id -> beersServiceClient.rxGetBeer(id.toString()))
        .map(response -> response.bodyAsJsonObject().getFloat("price"))
        .reduce(0f, Float::sum);
    customerSingle
      .zipWith(total, (jo, t) -> t)
      .flatMap(t -> orderServiceProxy.rxPlaceANewOrder(customerId, beersId, t))
      .flatMap(this::fillOrder)
      .subscribe(
        result ->
          routingContext
            .response()
            .setStatusCode(200)
            .putHeader("content-type", "application/json")
            .end(result.toJson().encode()),
        routingContext::fail
      );
  }
}
