package io.slinkydeveloper.brewery.api.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Shareable;

@DataObject(generateConverter = true, publicConverter = false)
public class ApiBeer implements Shareable {

  private String name;
  private Float price;
  private ApiStyle style;
  private Long id;

  public ApiBeer(
    String name,
    ApiStyle style,
    Float price,
    Long id
  ) {
    this.name = name;
    this.price = price;
    this.style = style;
    this.id = id;
  }

  public ApiBeer(JsonObject json) {
    ApiBeerConverter.fromJson(json, this);
  }

  public ApiBeer(ApiBeer other) {
    this.name = other.getName();
    this.price = other.getPrice();
    this.style = other.getStyle();
    this.id = other.getId();
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ApiBeerConverter.toJson(this, json);
    return json;
  }

  @Fluent public ApiBeer setName(String name){
    this.name = name;
    return this;
  }
  public String getName() {
    return this.name;
  }

  @Fluent public ApiBeer setPrice(Float price){
    this.price = price;
    return this;
  }
  public Float getPrice() {
    return this.price;
  }

  public ApiStyle getStyle() {
    return style;
  }

  @Fluent
  public ApiBeer setStyle(ApiStyle style) {
    this.style = style;
    return this;
  }

  @Fluent public ApiBeer setId(Long id){
    this.id = id;
    return this;
  }
  public Long getId() {
    return this.id;
  }

  @Override
  public Shareable copy() {
    return new ApiBeer(this);
  }

}
