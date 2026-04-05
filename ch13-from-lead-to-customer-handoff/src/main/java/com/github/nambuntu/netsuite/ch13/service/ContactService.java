package com.github.nambuntu.netsuite.ch13.service;

import com.github.nambuntu.netsuite.ch13.model.Contact;
import com.github.nambuntu.netsuite.ch13.store.InMemoryHandoffStore;
import java.util.Objects;

public final class ContactService {

  private final InMemoryHandoffStore store;

  public ContactService(InMemoryHandoffStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public Contact upsertContact(Contact contact) {
    return store.upsertContact(contact);
  }
}
