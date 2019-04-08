package io.slinkydeveloper.brewery.beers.client.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;

@DataObject(generateConverter = true, publicConverter = false)
public class NewBeer {

  private String name;
  private Long styleId;
  private Float price;

  public NewBeer (
    String name,
    Long styleId,
    Float price
  ) {
    this.name = name;
    this.styleId = styleId;
    this.price = price;
  }

  public NewBeer(JsonObject json) {
    NewBeerConverter.fromJson(json, this);
  }

  public NewBeer(NewBeer other) {
    this.name = other.getName();
    this.styleId = other.getStyleId();
    this.price = other.getPrice();
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    NewBeerConverter.toJson(this, json);
    return json;
  }

  @Fluent public NewBeer setName(String name){
    this.name = name;
    return this;
  }
  public String getName() {
    return this.name;
  }

  @Fluent public NewBeer setStyleId(Long styleId){
    this.styleId = styleId;
    return this;
  }
  public Long getStyleId() {
    return this.styleId;
  }

  @Fluent public NewBeer setPrice(Float price){
    this.price = price;
    return this;
  }
  public Float getPrice() {
    return this.price;
  }

}