package io.slinkydeveloper.brewery.api;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.SingleSource;
import io.slinkydeveloper.brewery.api.models.Beer;
import io.slinkydeveloper.brewery.api.models.Style;
import io.slinkydeveloper.brewery.beers.client.impl.BeersApiClientImpl;
import io.slinkydeveloper.brewery.beers.reactivex.client.BeersApiClient;
import io.slinkydeveloper.brewery.order.reactivex.api.OrderService;
import io.slinkydeveloper.brewery.styles.StyleId;
import io.slinkydeveloper.brewery.styles.StylesServiceGrpc;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBusOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.eventbus.bridge.tcp.TcpEventBusBridge;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.impl.AsyncResultSingle;

public class MainVerticle extends AbstractVerticle {

  // Get beers
  // Get styles
  // Add beer
  // - with new style
  // - with styleId
  // Add customer
  // Add order
  // Get order

  @Override
  public Completable rxStart() {
    // Create the clients
    BeersApiClient beersServiceClient = BeersApiClient.newInstance(new BeersApiClientImpl(vertx.getDelegate(), "localhost", 9001));
    StylesServiceGrpc.StylesServiceVertxStub stylesServiceClient = StylesServiceGrpc.newVertxStub(
      VertxChannelBuilder
        .forAddress(vertx.getDelegate(), "localhost", 9000)
        .usePlaintext(true)
        .build()
    );
    WebClient customersServiceClient = WebClient.create(vertx);
    OrderService orderServiceProxy = OrderService.createProxy(vertx, "orders.myapplication");

    Router router = Router.router(vertx);
    router
      .get("/beers")
      .handler(rc ->
        beersServiceClient
          .rxGetBeersList()
          .flatMapObservable(httpResponse -> Observable.fromIterable(httpResponse.bodyAsJsonArray()))
          .map(o -> new io.slinkydeveloper.brewery.beers.client.models.Beer((JsonObject)o))
          .<Beer>flatMapSingle(beer ->
            x -> AsyncResultSingle.<io.slinkydeveloper.brewery.styles.Style>
              toSingle(
                h -> stylesServiceClient.getStyle(StyleId.newBuilder().setId(beer.getStyleId()).build(), h)
              ).map(style ->
                new Beer(
                  beer.getName(),
                  new Style(
                    style.getId(),
                    style.getName(),
                    style.getDescription()
                  ),
                  beer.getPrice(),
                  beer.getId()
                )
              ).subscribe(x)
          )
          .collectInto(new JsonArray(), (j, beer) -> j.add(beer.toJson()))
          .subscribe(
            result ->
              rc
                .response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json")
                .end(result.encode()),
            rc::fail
          )
      );

    HttpServer server = vertx.createHttpServer();
    server.requestHandler(router);

    return Completable.fromSingle(server.rxListen(8000));
  }
}
