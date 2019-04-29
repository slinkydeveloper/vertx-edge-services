package io.slinkydeveloper.brewery.api;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.slinkydeveloper.brewery.api.models.ApiOrder;
import io.slinkydeveloper.brewery.beers.reactivex.client.BeersApiClient;
import io.slinkydeveloper.brewery.order.api.Order;
import io.slinkydeveloper.brewery.order.reactivex.api.OrderService;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.HttpResponse;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.client.predicate.ResponsePredicate;

import java.util.List;
import java.util.stream.Collectors;

public class OrdersHandlers {

  private BeersApiClient beersServiceClient;
  private RxCircuitBreaker beersCircuitBreaker;
  private WebClient customersServiceClient;
  private RxCircuitBreaker customersCircuitBreaker;
  private OrderService orderServiceProxy;
  private RxCircuitBreaker ordersCircuitBreaker;
  private BeersHandlers beersHandlers;

  public OrdersHandlers(BeersApiClient beersServiceClient, RxCircuitBreaker beersCircuitBreaker, WebClient customersServiceClient, RxCircuitBreaker customersCircuitBreaker, OrderService orderServiceProxy, RxCircuitBreaker ordersCircuitBreaker, BeersHandlers beersHandlers) {
    this.beersServiceClient = beersServiceClient;
    this.beersCircuitBreaker = beersCircuitBreaker;
    this.customersServiceClient = customersServiceClient;
    this.customersCircuitBreaker = customersCircuitBreaker;
    this.orderServiceProxy = orderServiceProxy;
    this.ordersCircuitBreaker = ordersCircuitBreaker;
    this.beersHandlers = beersHandlers;
  }

  public void handleGetOrder(RoutingContext routingContext) {
    this
      .orderServiceProxy
      .rxGetOrder(Long.parseLong(routingContext.pathParam("id")))
      .onErrorResumeNext(t -> Single.error(new HttpStatusException(503, "Service unavailable")))
      .flatMap(o -> (o == null) ? Single.error(new HttpStatusException(404, "Cannot find order")) : Single.just(o))
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
      .expect(ResponsePredicate.SC_SUCCESS)
      .expect(ResponsePredicate.JSON)
      .rxSendBuffer(Buffer.buffer("{\"query\":\"query {customer(id: \\\"" + order.getCustomerId() + "\\\"){id,name}}\\n\"}"))
      .onErrorResumeNext(t -> Single.error(new HttpStatusException(500, t)))
      .map(HttpResponse::bodyAsJsonObject)
      .map(jo -> jo.getJsonObject("data").getJsonObject("customer"))
      .flatMap(jo -> (jo != null) ? Single.just(jo) : Single.error(new HttpStatusException(404, "Cannot find customer")))
      .map(jo -> order.toJson().put("customerName", jo.getString("name")))
      .flatMap(jo ->
        Observable
          .fromIterable(jo.getJsonArray("beersId"))
          .flatMapSingle(o -> beersServiceClient.rxGetBeer(((Long)o).toString()))
          .onErrorResumeNext((Throwable t) -> Observable.error(new HttpStatusException(500, t)))
          .map(r -> new io.slinkydeveloper.brewery.beers.client.models.Beer(r.bodyAsJsonObject()))
          .onErrorResumeNext((Throwable t) -> Observable.error(new HttpStatusException(404, "Beer not found")))
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
