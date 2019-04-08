package io.slinkydeveloper.brewery.beers.client.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Shareable;

import java.util.List;
import java.util.Map;

@DataObject(generateConverter = true, publicConverter = false)
public class Beer implements Shareable {

  private String name;
  private Long styleId;
  private Float price;
  private Long id;

  public Beer (
    String name,
    Long styleId,
    Float price,
    Long id
  ) {
    this.name = name;
    this.styleId = styleId;
    this.price = price;
    this.id = id;
  }

  public Beer(JsonObject json) {
    BeerConverter.fromJson(json, this);
  }

  public Beer(Beer other) {
    this.name = other.getName();
    this.styleId = other.getStyleId();
    this.price = other.getPrice();
    this.id = other.getId();
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    BeerConverter.toJson(this, json);
    return json;
  }

  @Fluent public Beer setName(String name){
    this.name = name;
    return this;
  }
  public String getName() {
    return this.name;
  }

  @Fluent public Beer setStyleId(Long styleId){
    this.styleId = styleId;
    return this;
  }
  public Long getStyleId() {
    return this.styleId;
  }

  @Fluent public Beer setPrice(Float price){
    this.price = price;
    return this;
  }
  public Float getPrice() {
    return this.price;
  }

  @Fluent public Beer setId(Long id){
    this.id = id;
    return this;
  }
  public Long getId() {
    return this.id;
  }

  @Override
  public Shareable copy() {
    return new Beer(this);
  }

  public static Beer fromNewBeer(NewBeer newBeer) {
    Long id = System.currentTimeMillis();
    return new Beer(newBeer.getName(), newBeer.getStyleId(), newBeer.getPrice(), id);
  }

}
