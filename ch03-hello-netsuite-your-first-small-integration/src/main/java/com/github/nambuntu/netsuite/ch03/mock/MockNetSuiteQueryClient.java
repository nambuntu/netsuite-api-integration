package com.github.nambuntu.netsuite.ch03.mock;

import com.github.nambuntu.netsuite.ch03.client.NetSuiteQueryClient;
import com.github.nambuntu.netsuite.ch03.model.ContactInterest;

import java.util.Objects;
import java.util.Optional;

public class MockNetSuiteQueryClient implements NetSuiteQueryClient {

  private final InMemoryContactInterestStore store;

  public MockNetSuiteQueryClient(InMemoryContactInterestStore store) {
    this.store = Objects.requireNonNull(store, "store must not be null");
  }

  @Override
  public Optional<ContactInterest> findByExternalId(String externalId) {
    return store.findByExternalId(externalId);
  }
}
