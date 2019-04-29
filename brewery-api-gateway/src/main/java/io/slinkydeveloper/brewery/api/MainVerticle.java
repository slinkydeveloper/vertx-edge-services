package io.slinkydeveloper.brewery.api;

import io.reactivex.Completable;
import io.slinkydeveloper.brewery.beers.client.impl.BeersApiClientImpl;
import io.slinkydeveloper.brewery.beers.reactivex.client.BeersApiClient;
import io.slinkydeveloper.brewery.order.reactivex.api.OrderService;
import io.slinkydeveloper.brewery.styles.StylesServiceGrpc;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import io.vertx.grpc.VertxChannelBuilder;
import io.vertx.httpproxy.HttpProxy;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  @Override
  public Completable rxStart() {
    CircuitBreakerOptions circuitBreakerOptions = new CircuitBreakerOptions()
      .setMaxRetries(3);

    // Create the clients
    HttpClient beersServerClient = vertx.createHttpClient(new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(9001));
    HttpProxy beersServerProxy = BeersHandlers.configureBeersServiceProxy(beersServerClient);
    BeersApiClient beersServiceClient = BeersApiClient.newInstance(new BeersApiClientImpl(io.vertx.ext.web.client.WebClient.wrap(beersServerClient.getDelegate())));
    RxCircuitBreaker beersCircuitBreaker = RxCircuitBreaker.create("beers", vertx, circuitBreakerOptions);
    StylesServiceGrpc.StylesServiceVertxStub stylesServiceClient = StylesServiceGrpc.newVertxStub(
      VertxChannelBuilder
        .forAddress(vertx.getDelegate(), "localhost", 9000)
        .usePlaintext(true)
        .build()
    );
    RxCircuitBreaker stylesCircuitBreaker = RxCircuitBreaker.create("styles", vertx, circuitBreakerOptions);
    WebClient customersServiceClient = WebClient.create(vertx, new WebClientOptions().setDefaultHost("localhost").setDefaultPort(9003));
    RxCircuitBreaker customersCircuitBreaker = RxCircuitBreaker.create("customers", vertx, circuitBreakerOptions);
    OrderService orderServiceProxy = OrderService.createProxy(vertx, "orders.myapplication");
    RxCircuitBreaker ordersCircuitBreaker = RxCircuitBreaker.create("orders", vertx, circuitBreakerOptions);

    BeersHandlers beersHandlers = new BeersHandlers(beersServiceClient, beersServerProxy, beersCircuitBreaker, stylesServiceClient, stylesCircuitBreaker);
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
      .delete("/beer/:beerId")
      .handler(beersHandlers::handleRemoveBeer);

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
      if (rc.failure() instanceof HttpStatusException) {
        HttpStatusException failure = (HttpStatusException) rc.failure();
        JsonObject res = new JsonObject().put("statusCode", failure.getStatusCode()).put("message", failure.getPayload());
        if (failure.getCause() != null)
          res.put("cause", failure.getCause().toString());
        rc
          .response()
          .setStatusCode(failure.getStatusCode())
          .putHeader("content-type", "application/json")
          .end(res.encode());
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

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}
