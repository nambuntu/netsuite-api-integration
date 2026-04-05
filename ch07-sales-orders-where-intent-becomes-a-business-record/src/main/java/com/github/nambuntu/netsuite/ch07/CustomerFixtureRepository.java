package com.github.nambuntu.netsuite.ch07;

import java.util.LinkedHashMap;
import java.util.Map;

public final class CustomerFixtureRepository {

  private final Map<String, Customer> customersByExternalId = new LinkedHashMap<>();

  public CustomerFixtureRepository() {
    customersByExternalId.put("customer-theo-tran", new Customer("customer-theo-tran", "Theo Tran"));
  }

  public Customer require(String externalId) {
    Customer customer = customersByExternalId.get(externalId);
    if (customer == null) {
      throw new IllegalArgumentException("unknown customer external ID: " + externalId);
    }
    return customer;
  }
}
