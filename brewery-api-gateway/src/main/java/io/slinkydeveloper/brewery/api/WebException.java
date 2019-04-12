package io.slinkydeveloper.brewery.api;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.http.HttpServerResponse;

public class WebException extends RuntimeException {

  int statusCode;
  String bodyMessage;

  public WebException(int statusCode, String bodyMessage, Throwable cause) {
    super("Request failed with status code " + statusCode + " and body message " + bodyMessage, cause);
    this.statusCode = statusCode;
    this.bodyMessage = bodyMessage;
  }

  public WebException(int statusCode, String bodyMessage) {
    this(statusCode, bodyMessage, null);
  }

  public WebException(int statusCode, Throwable cause) {
    this(statusCode, cause.getMessage(), cause);
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getBodyMessage() {
    return bodyMessage;
  }

  public void writeTextPlainResponse(HttpServerResponse response) {
    response
      .setStatusCode(this.statusCode)
      .putHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
      .end(bodyMessage);
  }

  public void writeJsonResponse(HttpServerResponse response) {
    response
      .setStatusCode(this.statusCode)
      .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
      .end(new JsonObject().put("code", statusCode).put("message", bodyMessage).encode());
  }
}
