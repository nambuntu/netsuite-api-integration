package com.github.nambuntu.netsuite.ch13.service;

import com.github.nambuntu.netsuite.ch13.model.Lead;
import com.github.nambuntu.netsuite.ch13.model.Opportunity;
import com.github.nambuntu.netsuite.ch13.model.Prospect;
import com.github.nambuntu.netsuite.ch13.store.InMemoryHandoffStore;
import java.util.Objects;

public final class CrmStageService {

  private final InMemoryHandoffStore store;

  public CrmStageService(InMemoryHandoffStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public Lead upsertLead(Lead lead) {
    return store.upsertLead(lead);
  }

  public Prospect upsertProspect(Prospect prospect) {
    return store.upsertProspect(prospect);
  }

  public Opportunity upsertOpportunity(Opportunity opportunity) {
    return store.upsertOpportunity(opportunity);
  }
}
