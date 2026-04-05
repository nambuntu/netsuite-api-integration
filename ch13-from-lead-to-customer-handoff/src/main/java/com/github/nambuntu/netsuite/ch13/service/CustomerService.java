package com.github.nambuntu.netsuite.ch13.service;

import com.github.nambuntu.netsuite.ch13.model.Customer;
import com.github.nambuntu.netsuite.ch13.store.InMemoryHandoffStore;
import java.util.Objects;

public final class CustomerService {

  private final InMemoryHandoffStore store;

  public CustomerService(InMemoryHandoffStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public Customer upsertCustomer(Customer customer) {
    return store.upsertCustomer(customer);
  }
}
