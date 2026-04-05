package com.github.nambuntu.netsuite.ch03.client;

import com.github.nambuntu.netsuite.ch03.model.ContactInterest;

public interface NetSuiteRecordClient {

  void upsertContactInterest(ContactInterest interest);
}
