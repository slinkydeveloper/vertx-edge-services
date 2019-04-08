package io.slinkydeveloper.brewery.beers.server;

import io.slinkydeveloper.brewery.beers.client.models.Beer;
import io.slinkydeveloper.brewery.beers.client.models.NewBeer;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.RequestParameters;

import java.util.Map;

public class BeersStore {

  Map<Long, Beer> beers;

  public BeersStore(Vertx vertx) {
    this.beers = vertx.sharedData().getLocalMap("beers");
  }

  public void handleAddBeer(RoutingContext context) {
    RequestParameters params = context.get("parsedParameters");
    Beer b = Beer.fromNewBeer(new NewBeer(params.body().getJsonObject()));
    beers.put(b.getId(), b);
    context.response().setStatusCode(200).end();
  }

  public void handleGetBeersList(RoutingContext context) {
    context
      .response()
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .end(beers
        .values()
        .stream()
        .map(Beer::toJson)
        .reduce(new JsonArray(), JsonArray::add, JsonArray::addAll)
        .toBuffer()
      );
  }

  public void handleGetBeer(RoutingContext context) {
    RequestParameters params = context.get("parsedParameters");
    Beer b = beers.get(params.pathParameter("beerId").getLong());
    if (b == null)
      context.response().setStatusCode(404).end();
    else
      context
        .response()
        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .end(b.toJson().toBuffer());
  }

  public void handleRemoveBeer(RoutingContext context) {
    RequestParameters params = context.get("parsedParameters");
    beers.remove(params.pathParameter("beerId").getLong());
    context.response().setStatusCode(200).end();
  }

}
