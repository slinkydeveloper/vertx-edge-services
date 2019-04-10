package io.slinkydeveloper.brewery.api;

import io.reactivex.Completable;
import io.slinkydeveloper.brewery.beers.client.impl.BeersApiClientImpl;
import io.slinkydeveloper.brewery.beers.reactivex.client.BeersApiClient;
import io.slinkydeveloper.brewery.order.reactivex.api.OrderService;
import io.slinkydeveloper.brewery.styles.StylesServiceGrpc;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

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
    WebClient customersServiceClient = WebClient.create(vertx, new WebClientOptions().setDefaultHost("localhost").setDefaultPort(9003));
    OrderService orderServiceProxy = OrderService.createProxy(vertx, "orders.myapplication");

    BeersHandlers beersHandlers = new BeersHandlers(beersServiceClient, stylesServiceClient);
    CustomersHandlers customersHandlers = new CustomersHandlers(customersServiceClient);
    OrdersHandlers ordersHandlers = new OrdersHandlers(beersServiceClient, stylesServiceClient, customersServiceClient, orderServiceProxy, beersHandlers);

    Router router = Router.router(vertx);
    router
      .route()
      .handler(BodyHandler.create());
    router
      .get("/beer")
      .handler(beersHandlers::handleGetBeers);
    router
      .post("/beer")
      .handler(beersHandlers::handlePostBeer);

    router
      .get("/customer")
      .handler(customersHandlers::handleGetCustomers);

    router
      .post("/customer")
      .handler(customersHandlers::handleAddCustomer);

    router
      .get("/order/:id")
      .handler(ordersHandlers::handleGetOrder);

    router
      .post("/order")
      .handler(ordersHandlers::handlePostOrder);

    router.errorHandler(500, rc -> {
      rc.failure().printStackTrace();
      rc
        .response()
        .setStatusCode(500)
        .end();
    });

    HttpServer server = vertx.createHttpServer();
    server.requestHandler(router);

    return Completable.fromSingle(server.rxListen(8000));
  }
}
