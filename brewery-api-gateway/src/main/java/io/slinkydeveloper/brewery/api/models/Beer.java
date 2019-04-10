package io.slinkydeveloper.brewery.api.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Shareable;

@DataObject(generateConverter = true, publicConverter = false)
public class Beer implements Shareable {

  private String name;
  private Float price;
  private Style style;
  private Long id;

  public Beer(
    String name,
    Style style,
    Float price,
    Long id
  ) {
    this.name = name;
    this.price = price;
    this.style = style;
    this.id = id;
  }

  public Beer(JsonObject json) {
    BeerConverter.fromJson(json, this);
  }

  public Beer(Beer other) {
    this.name = other.getName();
    this.price = other.getPrice();
    this.style = other.getStyle();
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

  @Fluent public Beer setPrice(Float price){
    this.price = price;
    return this;
  }
  public Float getPrice() {
    return this.price;
  }

  public Style getStyle() {
    return style;
  }

  @Fluent
  public Beer setStyle(Style style) {
    this.style = style;
    return this;
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

}
