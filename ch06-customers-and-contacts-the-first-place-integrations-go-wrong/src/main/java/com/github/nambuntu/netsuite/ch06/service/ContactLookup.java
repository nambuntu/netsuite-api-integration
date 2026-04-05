package com.github.nambuntu.netsuite.ch06.service;

import com.github.nambuntu.netsuite.ch06.model.Contact;
import com.github.nambuntu.netsuite.ch06.store.InMemoryEntityStore;
import java.util.Objects;
import java.util.Optional;

public final class ContactLookup {

  private final InMemoryEntityStore store;

  public ContactLookup(InMemoryEntityStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public Optional<Contact> findByExternalId(String externalId) {
    return store.findContactByExternalId(externalId);
  }

  public int countContacts() {
    return store.contactCount();
  }
}
