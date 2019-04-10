#!/usr/bin/env bash

cd styles-service/styles-service-server
mvn vertx:start

cd ../../orders-service/orders-service-server
mvn vertx:start

cd ../../beers-service/beers-service-server
mvn vertx:start

cd ../../customers-service
mvn vertx:start

cd ../brewery-api-gateway
mvn vertx:run
