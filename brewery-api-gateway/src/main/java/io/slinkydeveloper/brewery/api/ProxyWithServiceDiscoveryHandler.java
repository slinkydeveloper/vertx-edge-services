package io.slinkydeveloper.brewery.api;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.httpproxy.HttpProxy;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.HttpLocation;

public class ProxyWithServiceDiscoveryHandler implements Handler<RoutingContext> {

  private final HttpProxy proxy;
  private final ServiceDiscovery discovery;

  public ProxyWithServiceDiscoveryHandler(HttpProxy proxy, ServiceDiscovery discovery, String service) {
    this.proxy = proxy;
    this.discovery = discovery;
    configureServiceDiscovery(service);
  }

  private void configureServiceDiscovery(String service) {
    proxy.selector(req -> {
      Future<SocketAddress> addressFuture = Future.future();
      discovery.getRecord(new JsonObject().put("name", service), ar -> {
        if (ar.failed()) addressFuture.fail(ar.cause());
        else {
          HttpLocation location = new HttpLocation(ar.result().getLocation());
          addressFuture.complete(
            SocketAddress.inetSocketAddress(location.getPort(), location.getHost())
          );
        }
      });
      return addressFuture;
    });
  }

  @Override
  public void handle(RoutingContext rc) {
    proxy.handle(rc.request().getDelegate());
  }

  public static ProxyWithServiceDiscoveryHandler create(HttpProxy proxy, ServiceDiscovery discovery, String serviceName) {
    return new ProxyWithServiceDiscoveryHandler(proxy, discovery, serviceName);
  }
}
