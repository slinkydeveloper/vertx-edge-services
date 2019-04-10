#!/usr/bin/env bash

cd styles-service/styles-service-server
mvn vertx:stop

cd ../../orders-service/orders-service-server
mvn vertx:stop

cd ../../beers-service/beers-service-server
mvn vertx:stop

cd ../../customers-service
mvn vertx:stop

cd ../brewery-api-gateway
mvn vertx:stop
