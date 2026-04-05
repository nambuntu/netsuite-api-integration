package com.github.nambuntu.netsuite.ch03.mock;

import com.github.nambuntu.netsuite.ch03.client.NetSuiteRecordClient;
import com.github.nambuntu.netsuite.ch03.model.ContactInterest;

import java.util.Objects;

public class MockNetSuiteRecordClient implements NetSuiteRecordClient {

  private final InMemoryContactInterestStore store;

  public MockNetSuiteRecordClient(InMemoryContactInterestStore store) {
    this.store = Objects.requireNonNull(store, "store must not be null");
  }

  @Override
  public void upsertContactInterest(ContactInterest interest) {
    store.upsert(interest);
  }
}
