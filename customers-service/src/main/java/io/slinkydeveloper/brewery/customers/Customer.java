package io.slinkydeveloper.brewery.customers;

import io.vertx.core.shareddata.Shareable;

import java.util.Objects;

public class Customer implements Shareable {

  private long id;
  private String name;

  public Customer(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Customer(Customer other) {
    this(other.id, other.name);
  }

  public long getId() {
    return id;
  }

  public Customer setId(long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Customer setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public Shareable copy() {
    return new Customer(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Customer customer = (Customer) o;
    return id == customer.id &&
      Objects.equals(name, customer.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
