package com.github.nambuntu.netsuite.ch06.service;

import com.github.nambuntu.netsuite.ch06.model.Customer;
import com.github.nambuntu.netsuite.ch06.store.InMemoryEntityStore;
import java.util.Objects;
import java.util.Optional;

public final class CustomerLookup {

  private final InMemoryEntityStore store;

  public CustomerLookup(InMemoryEntityStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public Optional<Customer> findByExternalId(String externalId) {
    return store.findCustomerByExternalId(externalId);
  }

  public int countCustomers() {
    return store.customerCount();
  }
}
