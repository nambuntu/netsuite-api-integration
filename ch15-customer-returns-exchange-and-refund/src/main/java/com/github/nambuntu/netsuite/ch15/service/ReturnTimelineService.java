package com.github.nambuntu.netsuite.ch15.service;

import com.github.nambuntu.netsuite.ch15.model.ReturnProgressEvent;
import com.github.nambuntu.netsuite.ch15.store.InMemoryReturnStore;
import java.util.List;
import java.util.Objects;

public final class ReturnTimelineService {

  private final InMemoryReturnStore store;

  public ReturnTimelineService(InMemoryReturnStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public List<ReturnProgressEvent> findTimelineBySupportCase(String supportCaseExternalId) {
    return store.timeline(supportCaseExternalId);
  }
}
