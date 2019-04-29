package io.slinkydeveloper.brewery.api;

import io.reactivex.Single;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.reactivex.circuitbreaker.CircuitBreaker;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.impl.AsyncResultSingle;

import java.util.function.Supplier;

public class RxCircuitBreaker {

  CircuitBreaker breaker;

  public RxCircuitBreaker(CircuitBreaker breaker) {
    this.breaker = breaker;
  }

  public <T> Single<T> execute(Supplier<Single<T>> s) {
    return AsyncResultSingle.toSingle(h ->
        this.breaker
          .<T>execute(fut -> s.get().subscribe(fut::complete, fut::fail))
          .setHandler(h)
      );
  }

  public static RxCircuitBreaker create(String name, Vertx vertx, CircuitBreakerOptions options) {
    return new RxCircuitBreaker(CircuitBreaker.create(name, vertx, options));
  }
}
