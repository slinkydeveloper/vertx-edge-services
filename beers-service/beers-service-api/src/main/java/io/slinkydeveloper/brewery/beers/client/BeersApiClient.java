package io.slinkydeveloper.brewery.beers.client;

import io.slinkydeveloper.brewery.beers.client.models.NewBeer;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.client.HttpResponse;

@VertxGen
public interface BeersApiClient {
  void getBeersList(
    Handler<AsyncResult<HttpResponse>> handler);

  void addBeerWithEmptyBody(
    Handler<AsyncResult<HttpResponse>> handler);

  void addBeerWithJson(
    NewBeer body, Handler<AsyncResult<HttpResponse>> handler);

  void getBeer(
    String beerId,
    Handler<AsyncResult<HttpResponse>> handler);

  void removeBeer(
    String beerId,
    Handler<AsyncResult<HttpResponse>> handler);

  void close();
}
