#!/usr/bin/env bash

cd styles-service
mvn clean install

cd ../orders-service
mvn clean install

cd ../beers-service
mvn clean install

cd ../customers-service
mvn clean install

cd ../brewery-api-gateway
mvn clean install
