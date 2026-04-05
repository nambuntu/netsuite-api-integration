package com.github.nambuntu.netsuite.ch03.service;

import com.github.nambuntu.netsuite.ch03.client.NetSuiteQueryClient;
import com.github.nambuntu.netsuite.ch03.client.NetSuiteRecordClient;
import com.github.nambuntu.netsuite.ch03.model.ContactInterest;
import com.github.nambuntu.netsuite.ch03.model.InterestSignup;
import com.github.nambuntu.netsuite.ch03.model.SyncedContactInterestResult;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.Objects;

public class ContactInterestService {

  private static final String SYNCED_STATUS = "synced";

  private final NetSuiteRecordClient recordClient;
  private final NetSuiteQueryClient queryClient;

  public ContactInterestService(NetSuiteRecordClient recordClient, NetSuiteQueryClient queryClient) {
    this.recordClient = Objects.requireNonNull(recordClient, "recordClient must not be null");
    this.queryClient = Objects.requireNonNull(queryClient, "queryClient must not be null");
  }

  public SyncedContactInterestResult syncInterestSignup(InterestSignup signup) {
    Objects.requireNonNull(signup, "signup must not be null");

    String externalId = resolveExternalId(signup);
    ContactInterest interest = new ContactInterest(
        externalId,
        signup.displayName(),
        signup.email(),
        signup.productCode(),
        signup.campaignCode(),
        signup.signupTimestamp(),
        SYNCED_STATUS);

    recordClient.upsertContactInterest(interest);

    ContactInterest syncedInterest = queryClient.findByExternalId(externalId)
        .orElseThrow(() -> new IllegalStateException(
            "Expected synced contact interest for external ID " + externalId));

    return new SyncedContactInterestResult(
        syncedInterest.externalId(),
        syncedInterest.displayName(),
        syncedInterest.email(),
        syncedInterest.productCode(),
        syncedInterest.campaignCode(),
        syncedInterest.syncStatus());
  }

  private String resolveExternalId(InterestSignup signup) {
    if (signup.hasExternalId()) {
      return signup.externalId();
    }
    return "interest-" + slugify(signup.displayName()) + "-" + slugify(signup.productCode());
  }

  private String slugify(String value) {
    String normalized = Normalizer.normalize(value, Form.NFD)
        .replaceAll("\\p{M}+", "")
        .toLowerCase(Locale.ROOT)
        .replaceAll("[^a-z0-9]+", "-")
        .replaceAll("(^-+|-+$)", "");
    if (normalized.isBlank()) {
      throw new IllegalArgumentException("Cannot derive slug from value: " + value);
    }
    return normalized;
  }
}
