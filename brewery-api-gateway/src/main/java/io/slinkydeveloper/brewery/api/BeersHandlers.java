package io.slinkydeveloper.brewery.api;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.slinkydeveloper.brewery.api.models.ApiBeer;
import io.slinkydeveloper.brewery.api.models.ApiStyle;
import io.slinkydeveloper.brewery.beers.client.models.Beer;
import io.slinkydeveloper.brewery.beers.client.models.NewBeer;
import io.slinkydeveloper.brewery.beers.reactivex.client.BeersApiClient;
import io.slinkydeveloper.brewery.styles.NewStyle;
import io.slinkydeveloper.brewery.styles.Style;
import io.slinkydeveloper.brewery.styles.StyleId;
import io.slinkydeveloper.brewery.styles.StylesServiceGrpc;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.impl.AsyncResultSingle;

public class BeersHandlers {

  BeersApiClient beersServiceClient;
  RxCircuitBreaker beersCircuitBreaker;
  StylesServiceGrpc.StylesServiceVertxStub stylesServiceClient;
  RxCircuitBreaker stylesCircuitBreaker;

  public BeersHandlers(BeersApiClient beersServiceClient, RxCircuitBreaker beersCircuitBreaker, StylesServiceGrpc.StylesServiceVertxStub stylesServiceClient, RxCircuitBreaker stylesCircuitBreaker) {
    this.beersServiceClient = beersServiceClient;
    this.beersCircuitBreaker = beersCircuitBreaker;
    this.stylesServiceClient = stylesServiceClient;
    this.stylesCircuitBreaker = stylesCircuitBreaker;
  }

  public void handleGetBeers(RoutingContext rc) {
    beersCircuitBreaker
      .execute(
        () -> beersServiceClient
          .rxGetBeersList()
          .flatMap(res -> res.statusCode() != 200 ? Single.error(new HttpStatusException(500, "Beers service error")) : Single.just(res))
      )
      .onErrorResumeNext(t -> Single.error(new HttpStatusException(503, "Beers service not available", t)))
      .flatMapObservable(httpResponse -> Observable.fromIterable(httpResponse.bodyAsJsonArray()))
      .map(o -> new io.slinkydeveloper.brewery.beers.client.models.Beer((JsonObject)o))
      .flatMapSingle(this::solveStyleAndBuildApiBeer)
      .collectInto(new JsonArray(), (j, apiBeer) -> j.add(apiBeer.toJson()))
      .subscribe(
        result ->
          rc
            .response()
            .setStatusCode(200)
            .putHeader("content-type", "application/json")
            .end(result.encode()),
        rc::fail
      );
  }

  public void handlePostBeer(RoutingContext rc) {
    JsonObject body = rc.getBodyAsJson();
    ((body.containsKey("style")) ? addBeerAndStyle(body) : addBeer(body))
      .subscribe(
        result ->
          rc
            .response()
            .setStatusCode(200)
            .putHeader("content-type", "application/json")
            .end(result.toJson().encode()),
        rc::fail
      );
  }

  private Single<ApiBeer> addBeerAndStyle(JsonObject beer) {
    JsonObject style = beer.getJsonObject("style");
    return AsyncResultSingle.<io.slinkydeveloper.brewery.styles.StyleId>toSingle(
        h -> stylesServiceClient.addStyle(
          NewStyle
            .newBuilder()
            .setName(style.getString("name"))
            .setDescription(style.getString("description"))
            .build(), h)
      )
      .flatMap(styleId ->
        beersServiceClient
          .rxAddBeerWithJson(new NewBeer(beer.getString("name"), styleId.getId(), beer.getFloat("price")))
      )
      .map(o -> new io.slinkydeveloper.brewery.beers.client.models.Beer(o.bodyAsJsonObject()))
      .flatMap(this::solveStyleAndBuildApiBeer);
  }

  private Single<ApiBeer> addBeer(JsonObject beer) {
    return beersServiceClient
      .rxAddBeerWithJson(new io.slinkydeveloper.brewery.beers.client.models.NewBeer(
        beer.getString("name"),
        beer.getLong("styleId"),
        beer.getFloat("price")
      ))
      .map(o -> new io.slinkydeveloper.brewery.beers.client.models.Beer(o.bodyAsJsonObject()))
      .flatMap(this::solveStyleAndBuildApiBeer);
  }

  public Single<ApiBeer> solveStyleAndBuildApiBeer(Beer b) {
    return AsyncResultSingle.<io.slinkydeveloper.brewery.styles.Style>toSingle(
      h -> stylesServiceClient.getStyle(StyleId.newBuilder().setId(b.getStyleId()).build(), h)
    )
      .flatMap(s -> (!Style.getDefaultInstance().equals(s)) ? Single.just(s) : Single.error(new HttpStatusException(404, "Style not found")))
      .map(style ->
      new ApiBeer(
        b.getName(),
        new ApiStyle(
          style.getId(),
          style.getName(),
          style.getDescription()
        ),
        b.getPrice(),
        b.getId()
      )
    );
  }
}
