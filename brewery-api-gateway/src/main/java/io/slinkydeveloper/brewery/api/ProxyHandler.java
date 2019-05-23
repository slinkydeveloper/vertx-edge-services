package io.slinkydeveloper.brewery.api;

import io.vertx.core.Handler;
import io.vertx.httpproxy.HttpProxy;
import io.vertx.reactivex.ext.web.RoutingContext;

public class ProxyHandler implements Handler<RoutingContext> {

  private final HttpProxy proxy;

  public ProxyHandler(HttpProxy proxy) {
    this.proxy = proxy;
  }

  @Override
  public void handle(RoutingContext rc) {
    proxy.handle(rc.request().getDelegate());
  }

  public static ProxyHandler create(HttpProxy proxy) {
    return new ProxyHandler(proxy);
  }
}
