package com.github.nambuntu.netsuite.ch06.service;

import com.github.nambuntu.netsuite.ch06.model.ChannelAccountSnapshot;
import com.github.nambuntu.netsuite.ch06.model.Customer;
import com.github.nambuntu.netsuite.ch06.model.CustomerRef;
import com.github.nambuntu.netsuite.ch06.store.InMemoryEntityStore;
import java.util.Objects;
import java.util.Optional;

public final class CustomerService {

  private final InMemoryEntityStore store;
  private final CustomerLookup customerLookup;

  public CustomerService(InMemoryEntityStore store, CustomerLookup customerLookup) {
    this.store = Objects.requireNonNull(store, "store");
    this.customerLookup = Objects.requireNonNull(customerLookup, "customerLookup");
  }

  public CustomerUpsertResult upsertCustomer(ChannelAccountSnapshot snapshot) {
    store.assertCustomerExternalIdAvailable(snapshot.customerExternalId());

    Optional<Customer> existingCustomer = customerLookup.findByExternalId(snapshot.customerExternalId());
    Customer customer = existingCustomer
        .map(existing -> new Customer(
            existing.internalId(),
            existing.externalId(),
            snapshot.customerDisplayName(),
            snapshot.customerEmail(),
            snapshot.customerPhone(),
            true,
            snapshot.sourceChannel()))
        .orElseGet(() -> new Customer(
            store.nextCustomerInternalId(),
            snapshot.customerExternalId(),
            snapshot.customerDisplayName(),
            snapshot.customerEmail(),
            snapshot.customerPhone(),
            true,
            snapshot.sourceChannel()));

    boolean created = existingCustomer.isEmpty();
    store.saveCustomer(customer);
    return new CustomerUpsertResult(created, customer.toRef(), customer);
  }

  public record CustomerUpsertResult(boolean created, CustomerRef customerRef, Customer customer) {

    public CustomerUpsertResult {
      Objects.requireNonNull(customerRef, "customerRef");
      Objects.requireNonNull(customer, "customer");
    }
  }
}
