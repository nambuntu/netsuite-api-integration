package com.github.nambuntu.netsuite.ch03.service;

import com.github.nambuntu.netsuite.ch03.client.NetSuiteQueryClient;
import com.github.nambuntu.netsuite.ch03.model.ContactInterest;
import com.github.nambuntu.netsuite.ch03.model.InterestSignup;
import com.github.nambuntu.netsuite.ch03.model.SyncedContactInterestResult;
import com.github.nambuntu.netsuite.ch03.mock.InMemoryContactInterestStore;
import com.github.nambuntu.netsuite.ch03.mock.MockNetSuiteQueryClient;
import com.github.nambuntu.netsuite.ch03.mock.MockNetSuiteRecordClient;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ContactInterestServiceTest {

  @Test
  void syncsANewSignupAndReadsBackTheSyncedInterest() {
    ContactInterestHarness harness = new ContactInterestHarness();

    SyncedContactInterestResult result = harness.service().syncInterestSignup(theoSignup(
        null,
        "campaign-ai-pets-launch",
        Instant.parse("2026-03-01T09:30:00Z")));

    assertThat(result.externalId()).isEqualTo("interest-theo-tran-nova-fox");
    assertThat(result.contactDisplayName()).isEqualTo("Theo Tran");
    assertThat(result.email()).isEqualTo("theo@aipets.example");
    assertThat(result.productCode()).isEqualTo("NOVA_FOX");
    assertThat(result.campaignCode()).isEqualTo("campaign-ai-pets-launch");
    assertThat(result.syncStatus()).isEqualTo("synced");
    assertThat(harness.store().size()).isEqualTo(1);
  }

  @Test
  void syncingTheSameExternalIdTwiceKeepsOneLogicalRecord() {
    ContactInterestHarness harness = new ContactInterestHarness();

    SyncedContactInterestResult first = harness.service().syncInterestSignup(theoSignup(
        null,
        "campaign-ai-pets-launch",
        Instant.parse("2026-03-01T09:30:00Z")));

    SyncedContactInterestResult second = harness.service().syncInterestSignup(theoSignup(
        first.externalId(),
        "campaign-ai-pets-launch",
        Instant.parse("2026-03-01T09:31:00Z")));

    assertThat(second.externalId()).isEqualTo(first.externalId());
    assertThat(harness.store().size()).isEqualTo(1);
  }

  @Test
  void returnsTheLookupResultRatherThanEchoingTheInput() {
    InMemoryContactInterestStore store = new InMemoryContactInterestStore();
    NetSuiteQueryClient queryClient = externalId -> Optional.of(new ContactInterest(
        externalId,
        "Theo Tran",
        "lookup@aipets.example",
        "NOVA_FOX",
        "campaign-from-query",
        Instant.parse("2026-03-01T09:45:00Z"),
        "looked-up"));
    ContactInterestService service = new ContactInterestService(
        new MockNetSuiteRecordClient(store),
        queryClient);

    SyncedContactInterestResult result = service.syncInterestSignup(theoSignup(
        null,
        "campaign-ai-pets-launch",
        Instant.parse("2026-03-01T09:30:00Z")));

    assertThat(result.email()).isEqualTo("lookup@aipets.example");
    assertThat(result.campaignCode()).isEqualTo("campaign-from-query");
    assertThat(result.syncStatus()).isEqualTo("looked-up");
  }

  @Test
  void repeatedSignupCanUpdateCampaignWhilePreservingIdentity() {
    ContactInterestHarness harness = new ContactInterestHarness();

    SyncedContactInterestResult first = harness.service().syncInterestSignup(theoSignup(
        null,
        "campaign-ai-pets-launch",
        Instant.parse("2026-03-01T09:30:00Z")));

    SyncedContactInterestResult updated = harness.service().syncInterestSignup(theoSignup(
        first.externalId(),
        "campaign-ai-pets-relaunch",
        Instant.parse("2026-03-02T10:15:00Z")));

    assertThat(updated.externalId()).isEqualTo(first.externalId());
    assertThat(updated.campaignCode()).isEqualTo("campaign-ai-pets-relaunch");
    assertThat(harness.store().size()).isEqualTo(1);
    assertThat(harness.store().findByExternalId(first.externalId()))
        .map(ContactInterest::signupTimestamp)
        .hasValue(Instant.parse("2026-03-02T10:15:00Z"));
  }

  private static InterestSignup theoSignup(String externalId, String campaignCode, Instant signupTimestamp) {
    return new InterestSignup(
        externalId,
        "Theo",
        "Tran",
        "theo@aipets.example",
        "NOVA_FOX",
        campaignCode,
        signupTimestamp);
  }

  private static final class ContactInterestHarness {
    private final InMemoryContactInterestStore store = new InMemoryContactInterestStore();
    private final ContactInterestService service = new ContactInterestService(
        new MockNetSuiteRecordClient(store),
        new MockNetSuiteQueryClient(store));

    private ContactInterestService service() {
      return service;
    }

    private InMemoryContactInterestStore store() {
      return store;
    }
  }
}
