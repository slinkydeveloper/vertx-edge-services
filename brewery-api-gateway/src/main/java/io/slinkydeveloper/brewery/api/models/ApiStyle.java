package io.slinkydeveloper.brewery.api.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, publicConverter = false)
public class ApiStyle {

  Long styleId;
  String name;
  String description;

  public ApiStyle(Long styleId, String name, String description) {
    this.styleId = styleId;
    this.name = name;
    this.description = description;
  }

  public ApiStyle(JsonObject obj) {
    ApiStyleConverter.fromJson(obj, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    ApiStyleConverter.toJson(this, json);
    return json;
  }

  public Long getStyleId() {
    return styleId;
  }

  @Fluent
  public ApiStyle setStyleId(Long styleId) {
    this.styleId = styleId;
    return this;
  }

  public String getName() {
    return name;
  }

  @Fluent
  public ApiStyle setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  @Fluent
  public ApiStyle setDescription(String description) {
    this.description = description;
    return this;
  }
}
