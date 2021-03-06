# Vert.x Edge Services Experiment

This repo contains some experimental implementation of microservices and an Edge service with Vert.x

There are five subprojects:

* Beers service: OpenAPI service with client generated with pmlopes' vertx-starter and server using vertx-web-api-contract
* Customers service: GraphQL service with vertx-web-graphql
* Orders service: Event bus service with Vert.x Service proxy
* Beer Styles service: gRPC service with vertx-grpc
* Brewery API Gateway: API Gateway built with vertx-web and Rx2

## Start/Stop scripts

There are a couple of helper scripts to start everything. To build:

```bash
mvn clean install
```

## TODO

* Improve doc of this experiment and start/stop things
* Experiment gateway creation with Kotlin
* Experiment gateway creation with vertx-web-api-contract/vertx-web-api-service (proxying to plain service-proxy?)
* Rate limiting/quota
* AuthN/Z
* Reverse proxying (Not working!!!)
* Monitoring
* Deploy challenges
