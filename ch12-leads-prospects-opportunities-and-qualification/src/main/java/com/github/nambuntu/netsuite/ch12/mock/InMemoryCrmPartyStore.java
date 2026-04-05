package com.github.nambuntu.netsuite.ch12.mock;

import com.github.nambuntu.netsuite.ch12.model.CrmParty;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryCrmPartyStore {

  private final Map<String, CrmParty> crmPartiesByExternalId = new LinkedHashMap<>();

  public CrmParty upsert(CrmParty crmParty) {
    crmPartiesByExternalId.put(crmParty.externalId(), crmParty);
    return crmParty;
  }

  public Optional<CrmParty> findByExternalId(String externalId) {
    return Optional.ofNullable(crmPartiesByExternalId.get(externalId));
  }

  public int count() {
    return crmPartiesByExternalId.size();
  }
}
