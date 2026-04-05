package com.github.nambuntu.netsuite.ch13.service;

import com.github.nambuntu.netsuite.ch13.model.HandoffIdentity;
import com.github.nambuntu.netsuite.ch13.model.LeadToCustomerHandoffRequest;
import java.util.Locale;
import java.util.Objects;

public final class HandoffIdentityResolver {

  public HandoffIdentity resolve(LeadToCustomerHandoffRequest request) {
    Objects.requireNonNull(request, "request");

    String expectedBase = normalizedPartySuffix(request.partyKey());
    assertMatches(expectedBase, request.leadExternalId(), "lead");
    assertMatches(expectedBase, request.prospectExternalId(), "prospect");
    assertMatches(expectedBase, request.contactExternalId(), "contact");
    assertMatches(expectedBase, request.customerExternalId(), "customer");

    if (!request.opportunityExternalId().startsWith("opp-")) {
      throw new IllegalArgumentException("Opportunity external ID must start with opp-");
    }

    return new HandoffIdentity(
        request.partyKey(),
        request.leadExternalId(),
        request.prospectExternalId(),
        request.opportunityExternalId(),
        request.contactExternalId(),
        request.customerExternalId());
  }

  private static String normalizedPartySuffix(String partyKey) {
    if (!partyKey.startsWith("party-")) {
      throw new IllegalArgumentException("partyKey must start with party-");
    }
    return partyKey.substring("party-".length()).toLowerCase(Locale.ROOT);
  }

  private static void assertMatches(String expectedBase, String externalId, String prefix) {
    String expected = prefix + "-" + expectedBase;
    if (!externalId.equals(expected)) {
      throw new IllegalArgumentException(
          "Expected " + prefix + " external ID to be " + expected + " but was " + externalId);
    }
  }
}
