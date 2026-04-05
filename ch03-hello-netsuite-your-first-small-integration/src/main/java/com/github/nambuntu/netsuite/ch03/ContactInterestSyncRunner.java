package com.github.nambuntu.netsuite.ch03;

import com.github.nambuntu.netsuite.ch03.model.InterestSignup;
import com.github.nambuntu.netsuite.ch03.model.SyncedContactInterestResult;
import com.github.nambuntu.netsuite.ch03.mock.InMemoryContactInterestStore;
import com.github.nambuntu.netsuite.ch03.mock.MockNetSuiteQueryClient;
import com.github.nambuntu.netsuite.ch03.mock.MockNetSuiteRecordClient;
import com.github.nambuntu.netsuite.ch03.service.ContactInterestService;

import java.time.Instant;

public final class ContactInterestSyncRunner {

  private ContactInterestSyncRunner() {
  }

  public static void main(String[] args) {
    InterestSignup signup = new InterestSignup(
        null,
        "Theo",
        "Tran",
        "theo@aipets.example",
        "NOVA_FOX",
        "campaign-ai-pets-launch",
        Instant.parse("2026-03-01T09:30:00Z"));

    System.out.println("Received signup for " + signup.displayName() + " and product " + signup.productCode());

    InMemoryContactInterestStore store = new InMemoryContactInterestStore();
    ContactInterestService service = new ContactInterestService(
        new MockNetSuiteRecordClient(store),
        new MockNetSuiteQueryClient(store));

    SyncedContactInterestResult result = service.syncInterestSignup(signup);

    System.out.println("Syncing contact interest using external ID " + result.externalId());
    System.out.println("Lookup confirmed synced record for "
        + result.contactDisplayName() + " <" + result.email() + ">");
    System.out.println("Campaign: " + result.campaignCode());
    System.out.println("Status: " + result.syncStatus());
  }
}
