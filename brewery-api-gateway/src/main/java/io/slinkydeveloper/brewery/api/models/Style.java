package io.slinkydeveloper.brewery.api.models;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonObject;

@DataObject(generateConverter = true, publicConverter = false)
public class Style {

  Long styleId;
  String name;
  String description;

  public Style(Long styleId, String name, String description) {
    this.styleId = styleId;
    this.name = name;
    this.description = description;
  }

  public Style(JsonObject obj) {
    StyleConverter.fromJson(obj, this);
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    StyleConverter.toJson(this, json);
    return json;
  }

  public Long getStyleId() {
    return styleId;
  }

  @Fluent
  public Style setStyleId(Long styleId) {
    this.styleId = styleId;
    return this;
  }

  public String getName() {
    return name;
  }

  @Fluent
  public Style setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  @Fluent
  public Style setDescription(String description) {
    this.description = description;
    return this;
  }
}
