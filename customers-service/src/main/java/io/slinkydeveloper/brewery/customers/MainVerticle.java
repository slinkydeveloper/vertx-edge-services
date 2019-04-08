package io.slinkydeveloper.brewery.customers;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphQLHandler;
import io.vertx.ext.web.handler.graphql.GraphQLHandlerOptions;
import io.vertx.ext.web.handler.graphql.VertxDataFetcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

public class MainVerticle extends AbstractVerticle {

  public static String schema;

  static {
    try {
      schema = String.join("", Files.readAllLines(Paths.get("src", "main", "resources", "customers.graphqls")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {

    Router router = Router.router(vertx);
    router
      .route("/graphql")
      .handler(GraphQLHandler.create(startupGraphQL(), new GraphQLHandlerOptions()));

    HttpServer server = vertx.createHttpServer(new HttpServerOptions().setPort(9003).setHost("localhost"));
    server.requestHandler(router).listen(h -> startFuture.complete());
  }

  private GraphQL startupGraphQL() {
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

    CustomersDataFetchers data = new CustomersDataFetchers(vertx);

    RuntimeWiring runtimeWiring = newRuntimeWiring()
      .type("Query", builder -> builder
        .dataFetcher("customers", new VertxDataFetcher<>(data::customers))
        .dataFetcher("customer", new VertxDataFetcher<>(data::customer))
      )
      .type("Mutation", builder -> builder
        .dataFetcher("addCustomer", new VertxDataFetcher<>(data::addCustomer))
        .dataFetcher("removeCustomer", new VertxDataFetcher<>(data::removeCustomer))
      ).build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);

    return GraphQL.newGraphQL(graphQLSchema).build();
  }



}
