package io.slinkydeveloper.brewery.api;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
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

  @Override
  public Completable rxStart() {
    CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(
      CircuitBreakerConfig.custom()
        .build()
    );

    // Create the clients
    BeersApiClient beersServiceClient = BeersApiClient.newInstance(new BeersApiClientImpl(vertx.getDelegate(), "localhost", 9001));
    CircuitBreaker beersCircuitBreaker = registry.circuitBreaker("beers");
    StylesServiceGrpc.StylesServiceVertxStub stylesServiceClient = StylesServiceGrpc.newVertxStub(
      VertxChannelBuilder
        .forAddress(vertx.getDelegate(), "localhost", 9000)
        .usePlaintext(true)
        .build()
    );
    CircuitBreaker stylesCircuitBreaker = registry.circuitBreaker("styles");
    WebClient customersServiceClient = WebClient.create(vertx, new WebClientOptions().setDefaultHost("localhost").setDefaultPort(9003));
    CircuitBreaker customersCircuitBreaker = registry.circuitBreaker("customers");
    OrderService orderServiceProxy = OrderService.createProxy(vertx, "orders.myapplication");
    CircuitBreaker ordersCircuitBreaker = registry.circuitBreaker("orders");

    BeersHandlers beersHandlers = new BeersHandlers(beersServiceClient, beersCircuitBreaker, stylesServiceClient, stylesCircuitBreaker);
    CustomersHandlers customersHandlers = new CustomersHandlers(customersServiceClient);
    OrdersHandlers ordersHandlers = new OrdersHandlers(beersServiceClient, beersCircuitBreaker, customersServiceClient, customersCircuitBreaker, orderServiceProxy, ordersCircuitBreaker, beersHandlers);

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
      if (rc.failure() instanceof WebException) {
        ((WebException)rc.failure()).writeJsonResponse(rc.response());
      } else {
        rc
          .response()
          .setStatusCode(500)
          .end();
      }
    });

    HttpServer server = vertx.createHttpServer();
    server.requestHandler(router);

    return Completable.fromSingle(server.rxListen(8000));
  }
}
