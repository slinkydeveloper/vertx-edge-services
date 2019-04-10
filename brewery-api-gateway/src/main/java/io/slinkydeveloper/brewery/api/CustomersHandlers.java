package io.slinkydeveloper.brewery.api;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.buffer.Buffer;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.client.WebClient;

public class CustomersHandlers {

  WebClient customersClient;

  public CustomersHandlers(WebClient customersClient) {
    this.customersClient = customersClient;
  }

  public void handleAddCustomer(RoutingContext routingContext) {
    JsonObject jsonObject = routingContext.getBodyAsJson();
    customersClient
      .post("/graphql")
      .rxSendBuffer(Buffer.buffer("{\"query\":\"mutation {addCustomer(name: \\\"" + jsonObject.getString("name") + "\\\"){id,name}}\\n\"}"))
      .map(r -> r.bodyAsJsonObject().getJsonObject("data").getJsonObject("addCustomer"))
      .subscribe(jo ->
        routingContext
          .response()
          .putHeader(HttpHeaders.CONTENT_TYPE.toString(), "application/json")
          .end(jo.encode()),
        routingContext::fail
      );
  }

  public void handleGetCustomers(RoutingContext routingContext) {
    customersClient
      .post("/graphql")
      .rxSendBuffer(Buffer.buffer("{\"query\":\"query {customers{id,name}}\\n\"}"))
      .map(r -> r.bodyAsJsonObject().getJsonObject("data").getJsonArray("customers"))
      .subscribe(ja ->
          routingContext
            .response()
            .putHeader(HttpHeaders.CONTENT_TYPE.toString(), "application/json")
            .end(ja.encode()),
        routingContext::fail
      );
  }

}
