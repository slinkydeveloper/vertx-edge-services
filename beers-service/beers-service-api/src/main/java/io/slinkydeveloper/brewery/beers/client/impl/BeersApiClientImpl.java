package io.slinkydeveloper.brewery.beers.client.impl;

import io.slinkydeveloper.brewery.beers.client.BeersApiClient;
import io.slinkydeveloper.brewery.beers.client.models.NewBeer;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeersApiClientImpl implements BeersApiClient {
    private WebClient client;
    private int port;
    private String host;

    private MultiMap cookieParams;

    public BeersApiClientImpl(Vertx vertx, String host, int port) {
        client = WebClient.create(vertx, new WebClientOptions().setDefaultHost(host).setDefaultPort(port));
        this.port = port;
        this.host = host;

        cookieParams = MultiMap.caseInsensitiveMultiMap();
    }

    public BeersApiClientImpl(WebClient client) {
        this.client = client;

        cookieParams = MultiMap.caseInsensitiveMultiMap();
    }

    /**
     * Call getBeersList with empty body. 
     * @param handler The handler for the asynchronous request
     */
    @Override
    public void getBeersList(
      Handler<AsyncResult<HttpResponse>> handler) {
        // Check required params
        

        // Generate the uri
        String uri = "/beer";

        HttpRequest request = client.get(uri);

        MultiMap requestCookies = MultiMap.caseInsensitiveMultiMap();

        this.renderAndAttachCookieHeader(request, requestCookies);
        request.send(handler);
    }

    /**
     * Call addBeer with empty body. 
     * @param handler The handler for the asynchronous request
     */
    @Override
    public void addBeerWithEmptyBody(
      Handler<AsyncResult<HttpResponse>> handler) {
        // Check required params
        

        // Generate the uri
        String uri = "/beer";

        HttpRequest request = client.post(uri);

        MultiMap requestCookies = MultiMap.caseInsensitiveMultiMap();

        this.renderAndAttachCookieHeader(request, requestCookies);
        request.send(handler);
    }

    /**
     * Call addBeer with Json body. 
     * @param body  that represents the body of the request
     * @param handler The handler for the asynchronous request
     */
    @Override
    public void addBeerWithJson(
      NewBeer body, Handler<AsyncResult<HttpResponse>> handler) {
        // Check required params
        

        // Generate the uri
        String uri = "/beer";

        HttpRequest request = client.post(uri);

        MultiMap requestCookies = MultiMap.caseInsensitiveMultiMap();
        this.addHeaderParam("Content-Type", "application/json", request);

        this.renderAndAttachCookieHeader(request, requestCookies);
        request.sendJson(body.toJson(), handler);
    }

    /**
     * Call getBeer with empty body. 
     * @param beerId Parameter beerId inside path
     * @param handler The handler for the asynchronous request
     */
    @Override
    public void getBeer(
      String beerId,
      Handler<AsyncResult<HttpResponse>> handler) {
        // Check required params
        if (beerId == null) throw new NullPointerException("Missing parameter beerId in path");
        

        // Generate the uri
        String uri = "/beer/{beerId}";
        uri = uri.replaceAll("\\{{1}([.;?*+]*([^\\{\\}.;?*+]+)[^\\}]*)\\}{1}", "{$2}"); //Remove * . ; ? from url template
        uri = uri.replace("{beerId}", this.renderPathParam("beerId", beerId));
        

        HttpRequest request = client.get(uri);

        MultiMap requestCookies = MultiMap.caseInsensitiveMultiMap();

        this.renderAndAttachCookieHeader(request, requestCookies);
        request.send(handler);
    }

    /**
     * Call removeBeer with empty body. 
     * @param beerId Parameter beerId inside path
     * @param handler The handler for the asynchronous request
     */
    @Override
    public void removeBeer(
      String beerId,
      Handler<AsyncResult<HttpResponse>> handler) {
        // Check required params
        if (beerId == null) throw new NullPointerException("Missing parameter beerId in path");
        

        // Generate the uri
        String uri = "/beer/{beerId}";
        uri = uri.replaceAll("\\{{1}([.;?*+]*([^\\{\\}.;?*+]+)[^\\}]*)\\}{1}", "{$2}"); //Remove * . ; ? from url template
        uri = uri.replace("{beerId}", this.renderPathParam("beerId", beerId));
        

        HttpRequest request = client.delete(uri);

        MultiMap requestCookies = MultiMap.caseInsensitiveMultiMap();

        this.renderAndAttachCookieHeader(request, requestCookies);
        request.send(handler);
    }


    

    // Parameters functions

    /**
     * Remove a cookie parameter from the cookie cache
     *
     * @param paramName name of cookie parameter
     */
    public void removeCookie(String paramName) {
        cookieParams.remove(paramName);
    }

    private void addQueryParam(String paramName, Object value, HttpRequest request) {
        request.addQueryParam(paramName, String.valueOf(value));
    }

    /**
     * Add a cookie param in cookie cache
     *
     * @param paramName name of cookie parameter
     * @param value value of cookie parameter
     */
    public void addCookieParam(String paramName, Object value) {
        renderCookieParam(paramName, value, cookieParams);
    }

    private void addHeaderParam(String headerName, Object value, HttpRequest request) {
        request.putHeader(headerName, String.valueOf(value));
    }

    private String renderPathParam(String paramName, Object value) {
        return String.valueOf(value);
    }

    private void renderCookieParam(String paramName, Object value, MultiMap map) {
        map.remove(paramName);
        map.add(paramName, String.valueOf(value));
    }

    /**
     * Following this table to implement parsedParameters serialization
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | style          | explode | in            | array                               | object                                 |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | matrix         | false   | path          | ;color=blue,black,brown             | ;color=R,100,G,200,B,150               |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | matrix         | true    | path          | ;color=blue;color=black;color=brown | ;R=100;G=200;B=150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | label          | false   | path          | .blue.black.brown                   | .R.100.G.200.B.150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | label          | true    | path          | .blue.black.brown                   | .R=100.G=200.B=150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | false   | query, cookie | color=blue,black,brown              | color=R,100,G,200,B,150                |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | true    | query, cookie | color=blue&color=black&color=brown  | R=100&G=200&B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | false   | path, header  | blue,black,brown                    | R,100,G,200,B,150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | true    | path, header  | blue,black,brown                    | R=100,G=200,B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | spaceDelimited | false   | query         | blue%20black%20brown                | R%20100%20G%20200%20B%20150            |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | pipeDelimited  | false   | query         | blue|black|brown                    | R|100|G|200                            |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | deepObject     | true    | query         | n/a                                 | color[R]=100&color[G]=200&color[B]=150 |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     */

    /**
     * Render path value with matrix style exploded/not exploded
     *
     * @param paramName
     * @param value
     * @return
     */
    private String renderPathMatrix(String paramName, Object value) {
        return ";" + paramName + "=" + String.valueOf(value);
    }

    /**
     * Render path array with matrix style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | matrix         | false   | path          | ;color=blue,black,brown             | ;color=R,100,G,200,B,150               |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathArrayMatrix(String paramName, List<Object> values) {
        String serialized = String.join(",", values.stream().map(object -> encode(String.valueOf(object))).collect(Collectors.toList()));
        return ";" + paramName + "=" + serialized;
    }

    /**
     * Render path object with matrix style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | matrix         | false   | path          | ;color=blue,black,brown             | ;color=R,100,G,200,B,150               |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathObjectMatrix(String paramName, Map<String, Object> values) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(encode(String.valueOf(entry.getValue())));
        }
        String serialized = String.join(",", listToSerialize);
        return ";" + paramName + "=" + serialized;
    }

    /**
     * Render path array with matrix style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | matrix         | true    | path          | ;color=blue;color=black;color=brown | ;R=100;G=200;B=150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathArrayMatrixExplode(String paramName, List<Object> values) {
        return String.join("", values.stream().map(object -> ";" + paramName + "=" + encode(String.valueOf(object))).collect(Collectors.toList()));
    }

    /**
     * Render path object with matrix style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | matrix         | true    | path          | ;color=blue;color=black;color=brown | ;R=100;G=200;B=150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathObjectMatrixExplode(String paramName, Map<String, Object> values) {
      return String.join("", values.entrySet().stream().map(
        entry -> ";" + entry.getKey() + "=" + encode(String.valueOf(entry.getValue()))
      ).collect(Collectors.toList()));
    }

    /**
     * Render path value with label style exploded/not exploded
     *
     * @param paramName
     * @param value
     * @return
     */
    private String renderPathLabel(String paramName, Object value) {
        return "." + String.valueOf(value);
    }

    /**
     * Render path array with label style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | label          | false   | path          | .blue.black.brown                   | .R.100.G.200.B.150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathArrayLabel(String paramName, List<Object> values) {
        return "." + String.join(".", values.stream().map(object -> encode(String.valueOf(object))).collect(Collectors.toList()));
    }

    /**
     * Render path object with label style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | label          | false   | path          | .blue.black.brown                   | .R.100.G.200.B.150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathObjectLabel(String paramName, Map<String, Object> values) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(encode(String.valueOf(entry.getValue())));
        }
        return "." + String.join(".", listToSerialize);
    }

    /**
     * Render path array with label style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | label          | true    | path          | .blue.black.brown                   | .R=100.G=200.B=150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathArrayLabelExplode(String paramName, List<Object> values) {
        return renderPathArrayLabel(paramName, values);
    }

    /**
     * Render path object with label style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | label          | true    | path          | .blue.black.brown                   | .R=100.G=200.B=150                     |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathObjectLabelExplode(String paramName, Map<String, Object> values) {
        String result = "";
        for (Map.Entry<String, Object> value : values.entrySet())
            result = result.concat("." + value.getKey() + "=" + encode(String.valueOf(value.getValue())));
        return result;
    }

    /**
     * Render path array with simple style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | false   | path, header  | blue,black,brown                    | R,100,G,200,B,150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathArraySimple(String paramName, List<Object> values) {
        return String.join(",", values.stream().map(object -> encode(String.valueOf(object))).collect(Collectors.toList()));
    }

    /**
     * Render path object with simple style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | false   | path, header  | blue,black,brown                    | R,100,G,200,B,150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathObjectSimple(String paramName, Map<String, Object> values) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(encode(String.valueOf(entry.getValue())));
        }
        return String.join(",", listToSerialize);
    }

    /**
     * Render path array with simple style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | true    | path, header  | blue,black,brown                    | R=100,G=200,B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathArraySimpleExplode(String paramName, List<Object> values) {
        return renderPathArraySimple(paramName, values);
    }

    /**
     * Render path object with simple style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | true    | path, header  | blue,black,brown                    | R=100,G=200,B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @return
     */
    private String renderPathObjectSimpleExplode(String paramName, Map<String, Object> values) {
        return String.join(",",
          values.entrySet().stream().map((entry) -> entry.getKey() + "=" + encode(String.valueOf(entry.getValue()))).collect(Collectors.toList()));
    }

    /**
     * Add query array with form style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | false   | query, cookie | color=blue,black,brown              | color=R,100,G,200,B,150                |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryArrayForm(String paramName, List<Object> values, HttpRequest request) {
        String serialized = String.join(",", values.stream().map(object -> String.valueOf(object)).collect(Collectors.toList()));
        this.addQueryParam(paramName, serialized, request); // Encoding is done by WebClient
    }

    /**
     * Add query object with form style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | false   | query, cookie | color=blue,black,brown              | color=R,100,G,200,B,150                |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryObjectForm(String paramName, Map<String, Object> values, HttpRequest request) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(String.valueOf(entry.getValue()));
        }
        String serialized = String.join(",", listToSerialize);
        this.addQueryParam(paramName, serialized, request); // Encoding is done by WebClient
    }

    /**
     * Add cookie array with form style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | false   | query, cookie | color=blue,black,brown              | color=R,100,G,200,B,150                |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     */
    private void renderCookieArrayForm(String paramName, List<Object> values, MultiMap map) {
        String value = String.join(",", values.stream().map(object -> String.valueOf(object)).collect(Collectors.toList()));
        map.remove(paramName);
        map.add(paramName, value);
    }

    /**
     * Add a cookie array parameter in cookie cache
     *
     * @param paramName name of cookie parameter
     * @param values list of values of cookie parameter
     */
    public void addCookieArrayForm(String paramName, List<Object> values) {
        renderCookieArrayForm(paramName, values, cookieParams);
    }

    /**
     * Add cookie object with form style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | false   | query, cookie | color=blue,black,brown              | color=R,100,G,200,B,150                |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     */
    private void renderCookieObjectForm(String paramName, Map<String, Object> values, MultiMap map) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(String.valueOf(entry.getValue()));
        }
        String value = String.join(",", listToSerialize);
        map.remove(paramName);
        map.add(paramName, value);
    }

    /**
     * Add a cookie object parameter in cookie cache
     *
     * @param paramName name of cookie parameter
     * @param values map of values of cookie parameter
     */
    public void addCookieObjectForm(String paramName, Map<String, Object> values) {
        renderCookieObjectForm(paramName, values, cookieParams);
    }

    /**
     * Add query array with form style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | true    | query, cookie | color=blue&color=black&color=brown  | R=100&G=200&B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryArrayFormExplode(String paramName, List<Object> values, HttpRequest request) {
        for (Object value : values)
            this.addQueryParam(paramName, String.valueOf(value), request);
    }

    /**
     * Add query object with form style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | true    | query, cookie | color=blue&color=black&color=brown  | R=100&G=200&B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryObjectFormExplode(String paramName, Map<String, Object> values, HttpRequest request) {
        for (Map.Entry<String, Object> value : values.entrySet())
            this.addQueryParam(value.getKey(), String.valueOf(value.getValue()), request);
    }

    /**
     * Add cookie array with form style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | true    | query, cookie | color=blue&color=black&color=brown  | R=100&G=200&B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     */
    private void renderCookieArrayFormExplode(String paramName, List<Object> values, MultiMap map) {
        map.remove(paramName);
        for (Object value : values)
            map.add(paramName, String.valueOf(value));
    }

    public void addCookieArrayFormExplode(String paramName, List<Object> values) {
        renderCookieArrayFormExplode(paramName, values, cookieParams);
    }

    /**
     * Add cookie object with form style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | form           | true    | query, cookie | color=blue&color=black&color=brown  | R=100&G=200&B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     */
    private void renderCookieObjectFormExplode(String paramName, Map<String, Object> values, MultiMap map) {
        for (Map.Entry<String, Object> value : values.entrySet()) {
            map.remove(value.getKey());
            map.add(value.getKey(), String.valueOf(value.getValue()));
        }
    }

    public void addCookieObjectFormExplode(String paramName, Map<String, Object> values) {
        renderCookieObjectFormExplode(paramName, values, cookieParams);
    }

    /**
     * Add header array with simple style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | false   | path, header  | blue,black,brown                    | R,100,G,200,B,150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param headerName
     * @param values
     * @param request
     */
    private void addHeaderArraySimple(String headerName, List<Object> values, HttpRequest request) {
        String serialized = String.join(",", values.stream().map(object -> String.valueOf(object)).collect(Collectors.toList()));
        this.addHeaderParam(headerName, serialized, request);
    }

    /**
     * Add header object with simple style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | false   | path, header  | blue,black,brown                    | R,100,G,200,B,150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param headerName
     * @param values
     * @param request
     */
    private void addHeaderObjectSimple(String headerName, Map<String, Object> values, HttpRequest request) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(String.valueOf(entry.getValue()));
        }
        String serialized = String.join(",", listToSerialize);
        this.addHeaderParam(headerName, serialized, request);
    }

    /**
     * Add header array with simple style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | true    | path, header  | blue,black,brown                    | R=100,G=200,B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param headerName
     * @param values
     * @param request
     */
    private void addHeaderArraySimpleExplode(String headerName, List<Object> values, HttpRequest request) {
        this.addHeaderArraySimple(headerName, values, request);
    }

    /**
     * Add header object with simple style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | simple         | true    | path, header  | blue,black,brown                    | R=100,G=200,B=150                      |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param headerName
     * @param values
     * @param request
     */
    private void addHeaderObjectSimpleExplode(String headerName, Map<String, Object> values, HttpRequest request) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey() + "=" + String.valueOf(entry.getValue()));
        }
        String serialized = String.join(",", listToSerialize);
        this.addHeaderParam(headerName, serialized, request);
    }

    /**
     * Add query array with spaceDelimited style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | spaceDelimited | false   | query         | blue%20black%20brown                | R%20100%20G%20200%20B%20150            |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryArraySpaceDelimited(String paramName, List<Object> values, HttpRequest request) {
        String serialized = String.join(" ", values.stream().map(object -> String.valueOf(object)).collect(Collectors.toList()));
        this.addQueryParam(paramName, serialized, request); // Encoding is done by WebClient
    }

    /**
     * Add query object with spaceDelimited style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | spaceDelimited | false   | query         | blue%20black%20brown                | R%20100%20G%20200%20B%20150            |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryObjectSpaceDelimited(String paramName, Map<String, Object> values, HttpRequest request) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(String.valueOf(entry.getValue()));
        }
        String serialized = String.join(" ", listToSerialize);
        this.addQueryParam(paramName, serialized, request); // Encoding is done by WebClient
    }

    /**
     * Add query array with pipeDelimited style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | pipeDelimited  | false   | query         | blue|black|brown                    | R|100|G|200                            |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryArrayPipeDelimited(String paramName, List<Object> values, HttpRequest request) {
        String serialized = String.join("|", values.stream().map(object -> String.valueOf(object)).collect(Collectors.toList()));
        this.addQueryParam(paramName, serialized, request); // Encoding is done by WebClient
    }

    /**
     * Add query object with pipeDelimited style and not exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | pipeDelimited  | false   | query         | blue|black|brown                    | R|100|G|200                            |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryObjectPipeDelimited(String paramName, Map<String, Object> values, HttpRequest request) {
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            listToSerialize.add(entry.getKey());
            listToSerialize.add(String.valueOf(entry.getValue()));
        }
        String serialized = String.join("|", listToSerialize);
        this.addQueryParam(paramName, serialized, request); // Encoding is done by WebClient
    }

    /**
     * Add query object with deepObject style and exploded
     *
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     | deepObject     | true    | query         | n/a                                 | color[R]=100&color[G]=200&color[B]=150 |
     +----------------+---------+---------------+-------------------------------------+----------------------------------------+
     *
     * @param paramName
     * @param values
     * @param request
     */
    private void addQueryObjectDeepObjectExplode(String paramName, Map<String, Object> values, HttpRequest request) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            this.addQueryParam(paramName + "[" + entry.getKey() + "]", String.valueOf(entry.getValue()), request);
        }
    }


    private void renderAndAttachCookieHeader(HttpRequest request, MultiMap otherCookies) {
        if ((otherCookies == null || otherCookies.isEmpty()) && cookieParams.isEmpty())
            return;
        List<String> listToSerialize = new ArrayList<>();
        for (Map.Entry<String, String> e : cookieParams.entries()) {
            if (otherCookies!= null && !otherCookies.contains(e.getKey())) {
                try {
                    listToSerialize.add(URLEncoder.encode(e.getKey(), "UTF-8") + "=" + URLEncoder.encode(e.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                }
            }
        }
        if (otherCookies != null) {
            for (Map.Entry<String, String> e : otherCookies.entries()) {
                try {
                    listToSerialize.add(URLEncoder.encode(e.getKey(), "UTF-8") + "=" + URLEncoder.encode(e.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e1) {
                }
            }
        }
        request.putHeader("Cookie", String.join("; ", listToSerialize));
    }

    // Other functions

    private String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Close the connection with server
     *
     */
    @Override
    public void close() {
        client.close();
    }

}
