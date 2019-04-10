package io.slinkydeveloper.brewery.api.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

import java.util.List;

@DataObject(generateConverter = true)
public class ApiOrder {

  private long id;
  private long customerId;
  private String customerName;
  private List<ApiBeer> beers;
  private float total;

  public ApiOrder(long id, long customerId, String customerName, List<ApiBeer> beers, float total) {
    this.id = id;
    this.customerId = customerId;
    this.customerName = customerName;
    this.beers = beers;
    this.total = total;
  }

  public ApiOrder(JsonObject json) {
    ApiOrderConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ApiOrderConverter.toJson(this, json);
    return json;
  }

  public long getId() {
    return id;
  }

  @Fluent
  public ApiOrder setId(long id) {
    this.id = id;
    return this;
  }

  public long getCustomerId() {
    return customerId;
  }

  @Fluent
  public ApiOrder setCustomerId(long customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getCustomerName() {
    return customerName;
  }

  @Fluent
  public ApiOrder setCustomerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  public List<ApiBeer> getBeers() {
    return beers;
  }

  @Fluent
  public ApiOrder setBeers(List<ApiBeer> beers) {
    this.beers = beers;
    return this;
  }

  public float getTotal() {
    return total;
  }

  @Fluent
  public ApiOrder setTotal(float total) {
    this.total = total;
    return this;
  }
}
