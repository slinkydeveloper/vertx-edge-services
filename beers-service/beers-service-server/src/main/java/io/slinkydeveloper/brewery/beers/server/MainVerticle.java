package io.slinkydeveloper.brewery.beers.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpEndpoint;

public class MainVerticle extends AbstractVerticle {

  HttpServer server;
  ServiceDiscovery discovery;
  Record record;

  @Override
  public void start(Future<Void> future) {
    BeersStore beersStore = new BeersStore(vertx);
    discovery = ServiceDiscovery.create(vertx);

    OpenAPI3RouterFactory.create(this.vertx, "openapi.yaml", openAPI3RouterFactoryAsyncResult -> {
      if (openAPI3RouterFactoryAsyncResult.succeeded()) {
        OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();

        // Add routes handlers
        routerFactory.addHandlerByOperationId("getBeersList", beersStore::handleGetBeersList);
        routerFactory.addHandlerByOperationId("addBeer", beersStore::handleAddBeer);
        routerFactory.addHandlerByOperationId("getBeer", beersStore::handleGetBeer);
        routerFactory.addHandlerByOperationId("removeBeer", beersStore::handleRemoveBeer);

        // Generate the router
        Router router = routerFactory.getRouter();
        router.errorHandler(500, rc -> {
          rc.failure().printStackTrace(System.err);
          rc.response().setStatusCode(500).end(rc.failure().getMessage());
        });
        server = vertx
          .createHttpServer(new HttpServerOptions().setPort(9001).setHost("localhost"))
          .exceptionHandler(err -> {
            err.printStackTrace(System.err);
          });

        server.requestHandler(router).listen();

        // Let the service discovery find us
        record = HttpEndpoint.createRecord("beers", "localhost", 9001, "/");

        discovery.publish(record, ar -> future.complete());
      } else {
        future.fail(openAPI3RouterFactoryAsyncResult.cause());
      }
    });
  }

  @Override
  public void stop(Future<Void> fut){
    this.server.close();
    discovery.unpublish(record.getRegistration(), fut);
  }

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

}
