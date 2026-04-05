package com.github.nambuntu.netsuite.ch13.model;

import java.util.Objects;

public record LeadToCustomerHandoffRequest(
    String partyKey,
    String campaignExternalId,
    String leadExternalId,
    String prospectExternalId,
    String opportunityExternalId,
    String contactExternalId,
    String customerExternalId,
    String name,
    String primaryEmail,
    String opportunityTitle) {

  public LeadToCustomerHandoffRequest {
    Objects.requireNonNull(partyKey, "partyKey");
    Objects.requireNonNull(campaignExternalId, "campaignExternalId");
    Objects.requireNonNull(leadExternalId, "leadExternalId");
    Objects.requireNonNull(prospectExternalId, "prospectExternalId");
    Objects.requireNonNull(opportunityExternalId, "opportunityExternalId");
    Objects.requireNonNull(contactExternalId, "contactExternalId");
    Objects.requireNonNull(customerExternalId, "customerExternalId");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(primaryEmail, "primaryEmail");
    Objects.requireNonNull(opportunityTitle, "opportunityTitle");
  }
}
