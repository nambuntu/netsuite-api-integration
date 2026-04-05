package com.github.nambuntu.netsuite.ch13.service;

import com.github.nambuntu.netsuite.ch13.model.Contact;
import com.github.nambuntu.netsuite.ch13.model.Customer;
import com.github.nambuntu.netsuite.ch13.model.HandoffContinuityView;
import com.github.nambuntu.netsuite.ch13.model.Lead;
import com.github.nambuntu.netsuite.ch13.model.Opportunity;
import com.github.nambuntu.netsuite.ch13.model.Prospect;
import com.github.nambuntu.netsuite.ch13.store.InMemoryHandoffStore;
import java.util.Objects;

public final class HandoffContinuityReadService {

  private final InMemoryHandoffStore store;

  public HandoffContinuityReadService(InMemoryHandoffStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public HandoffContinuityView readContinuity(String partyKey) {
    Lead lead = store.findLeadByPartyKey(partyKey)
        .orElseThrow(() -> new IllegalArgumentException("Missing lead for partyKey " + partyKey));
    Prospect prospect = store.findProspectByPartyKey(partyKey)
        .orElseThrow(() -> new IllegalArgumentException("Missing prospect for partyKey " + partyKey));
    Opportunity opportunity = store.findOpportunityByPartyKey(partyKey)
        .orElseThrow(() -> new IllegalArgumentException("Missing opportunity for partyKey " + partyKey));
    Contact contact = store.findContactByPartyKey(partyKey)
        .orElseThrow(() -> new IllegalArgumentException("Missing contact for partyKey " + partyKey));
    Customer customer = store.findCustomerByPartyKey(partyKey)
        .orElseThrow(() -> new IllegalArgumentException("Missing customer for partyKey " + partyKey));

    String continuityStatus = contact.primaryEmail().equals(customer.primaryEmail()) ? "OK" : "REVIEW";
    String continuityNote = customer.campaignExternalId()
        + " -> " + opportunity.externalId()
        + " -> " + customer.externalId();

    return new HandoffContinuityView(
        partyKey,
        customer.campaignExternalId(),
        lead.externalId(),
        prospect.externalId(),
        opportunity.externalId(),
        contact.externalId(),
        customer.externalId(),
        contact.primaryEmail(),
        customer.primaryEmail(),
        continuityStatus,
        continuityNote);
  }
}
