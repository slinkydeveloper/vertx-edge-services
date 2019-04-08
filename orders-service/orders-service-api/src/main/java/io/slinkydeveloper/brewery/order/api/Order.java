package io.slinkydeveloper.brewery.order.api;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Shareable;

import java.util.List;

@DataObject(generateConverter = true)
public class Order implements Shareable {

  private long id;
  private long customerId;
  private List<Long> beersId;
  private float total;

  public Order(long id, long customerId, List<Long> beersId, float total) {
    this.id = id;
    this.customerId = customerId;
    this.beersId = beersId;
    this.total = total;
  }

  public Order(JsonObject json) {
    OrderConverter.fromJson(json, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    OrderConverter.toJson(this, json);
    return json;
  }

  public long getId() {
    return id;
  }

  @Fluent
  public Order setId(long id) {
    this.id = id;
    return this;
  }

  public long getCustomerId() {
    return customerId;
  }

  @Fluent
  public Order setCustomerId(long customerId) {
    this.customerId = customerId;
    return this;
  }

  public List<Long> getBeersId() {
    return beersId;
  }

  @Fluent
  public Order setBeersId(List<Long> beersId) {
    this.beersId = beersId;
    return this;
  }

  public float getTotal() {
    return total;
  }

  @Fluent
  public Order setTotal(float total) {
    this.total = total;
    return this;
  }

  @Override
  public Shareable copy() {
    return new Order(this.toJson());
  }
}
