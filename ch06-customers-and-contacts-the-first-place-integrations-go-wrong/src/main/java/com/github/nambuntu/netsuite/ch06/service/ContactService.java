package com.github.nambuntu.netsuite.ch06.service;

import com.github.nambuntu.netsuite.ch06.model.ChannelAccountSnapshot;
import com.github.nambuntu.netsuite.ch06.model.Contact;
import com.github.nambuntu.netsuite.ch06.model.ContactRef;
import com.github.nambuntu.netsuite.ch06.model.CustomerRef;
import com.github.nambuntu.netsuite.ch06.store.InMemoryEntityStore;
import java.util.Objects;
import java.util.Optional;

public final class ContactService {

  private final InMemoryEntityStore store;
  private final ContactLookup contactLookup;

  public ContactService(InMemoryEntityStore store, ContactLookup contactLookup) {
    this.store = Objects.requireNonNull(store, "store");
    this.contactLookup = Objects.requireNonNull(contactLookup, "contactLookup");
  }

  public ContactUpsertResult upsertPrimaryContact(ChannelAccountSnapshot snapshot, CustomerRef customerRef) {
    Objects.requireNonNull(customerRef, "customerRef");
    store.assertContactExternalIdAvailable(snapshot.contactExternalId());

    Optional<Contact> existingContact = contactLookup.findByExternalId(snapshot.contactExternalId());
    Contact contact = existingContact
        .map(existing -> new Contact(
            existing.internalId(),
            existing.externalId(),
            customerRef,
            snapshot.contactFirstName(),
            snapshot.contactLastName(),
            snapshot.contactEmail(),
            snapshot.contactPhone(),
            snapshot.contactRole(),
            true,
            snapshot.sourceChannel()))
        .orElseGet(() -> new Contact(
            store.nextContactInternalId(),
            snapshot.contactExternalId(),
            customerRef,
            snapshot.contactFirstName(),
            snapshot.contactLastName(),
            snapshot.contactEmail(),
            snapshot.contactPhone(),
            snapshot.contactRole(),
            true,
            snapshot.sourceChannel()));

    boolean created = existingContact.isEmpty();
    store.saveContact(contact);
    return new ContactUpsertResult(created, contact.toRef(), contact);
  }

  public record ContactUpsertResult(boolean created, ContactRef contactRef, Contact contact) {

    public ContactUpsertResult {
      Objects.requireNonNull(contactRef, "contactRef");
      Objects.requireNonNull(contact, "contact");
    }
  }
}
