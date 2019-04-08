package io.slinkydeveloper.brewery.styles.server;

import io.slinkydeveloper.brewery.styles.*;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.HashMap;
import java.util.Map;

public class StylesServiceGrpcImpl extends StylesServiceGrpc.StylesServiceVertxImplBase {

  Map<Long, Style> styles;

  public StylesServiceGrpcImpl() {
    this.styles = new HashMap<>();
  }

  @Override
  public void getStyle(StyleId request, Future<Style> response) {
    response.complete(this.styles.get(request.getId()));
  }

  @Override
  public void getStyles(Empty request, Future<Styles> response) {
    Styles.Builder b = Styles.newBuilder();
    styles.values().forEach(b::addStyles);
    response.complete(b.build());
  }

  @Override
  public void addStyle(NewStyle request, Future<StyleId> response) {
    Style style = Style
      .newBuilder()
      .setId(System.currentTimeMillis())
      .setName(request.getName())
      .setDescription(request.getDescription())
      .build();
    styles.put(style.getId(), style);
    response.complete(StyleId.newBuilder().setId(style.getId()).build());
  }

  @Override
  public void removeStyle(StyleId request, Future<Empty> response) {
    styles.remove(request.getId());
    response.complete(Empty.newBuilder().build());
  }
}
