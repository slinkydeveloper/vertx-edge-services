package io.slinkydeveloper.brewery.styles.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.grpc.VertxServerBuilder;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    VertxServerBuilder
      .forAddress(vertx, "localhost", 9000)
      .addService(new StylesServiceGrpcImpl())
      .build().start(startFuture);
  }
}
