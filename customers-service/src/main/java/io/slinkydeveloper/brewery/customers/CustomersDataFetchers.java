package io.slinkydeveloper.brewery.customers;

import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomersDataFetchers {

  private final Map<Long, Customer> customers;

  public CustomersDataFetchers(Vertx vertx) {
    this.customers = vertx.sharedData().getLocalMap("customers");
  }

  public void customers(DataFetchingEnvironment environment, Future<List<Customer>> fut) {
    fut.complete(new ArrayList<>(customers.values()));
  }

  public void customer(DataFetchingEnvironment environment, Future<Customer> fut) {
    String id = environment.getArgument("id");
    fut.complete(
      this.customers.get(Long.valueOf(id))
    );
  }

  public void addCustomer(DataFetchingEnvironment environment, Future<Customer> fut) {
    Customer newCustomer = new Customer(System.currentTimeMillis(), environment.getArgument("name"));
    customers.put(newCustomer.getId(), newCustomer);
    fut.complete(newCustomer);
  }

  public void removeCustomer(DataFetchingEnvironment environment, Future<Customer> fut) {
    String id = environment.getArgument("id");
    Customer removed = customers.remove(Long.valueOf(id));
    fut.complete(removed);
  }

}
