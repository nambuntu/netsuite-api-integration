package com.github.nambuntu.netsuite.ch03.mock;

import com.github.nambuntu.netsuite.ch03.model.ContactInterest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryContactInterestStore {

  private final Map<String, ContactInterest> interestsByExternalId = new LinkedHashMap<>();

  public void upsert(ContactInterest interest) {
    interestsByExternalId.put(interest.externalId(), interest);
  }

  public Optional<ContactInterest> findByExternalId(String externalId) {
    return Optional.ofNullable(interestsByExternalId.get(externalId));
  }

  public int size() {
    return interestsByExternalId.size();
  }
}
