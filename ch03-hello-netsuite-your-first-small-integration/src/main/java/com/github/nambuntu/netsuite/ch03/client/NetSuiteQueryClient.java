package com.github.nambuntu.netsuite.ch03.client;

import com.github.nambuntu.netsuite.ch03.model.ContactInterest;

import java.util.Optional;

public interface NetSuiteQueryClient {

  Optional<ContactInterest> findByExternalId(String externalId);
}
