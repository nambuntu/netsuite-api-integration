package com.github.nambuntu.netsuite.ch06.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nambuntu.netsuite.ch06.model.ChannelAccountSnapshot;
import com.github.nambuntu.netsuite.ch06.store.InMemoryEntityStore;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;

class ContactServiceTest {

  @Test
  void upsertContactKeepsStableIdentityAndCustomerLink() throws IOException {
    InMemoryEntityStore store = new InMemoryEntityStore();
    CustomerLookup customerLookup = new CustomerLookup(store);
    ContactLookup contactLookup = new ContactLookup(store);
    CustomerService customerService = new CustomerService(store, customerLookup);
    ContactService contactService = new ContactService(store, contactLookup);
    List<ChannelAccountSnapshot> snapshots = loadSnapshots();

    CustomerService.CustomerUpsertResult firstCustomer = customerService.upsertCustomer(snapshots.get(0));
    ContactService.ContactUpsertResult first =
        contactService.upsertPrimaryContact(snapshots.get(0), firstCustomer.customerRef());

    CustomerService.CustomerUpsertResult secondCustomer = customerService.upsertCustomer(snapshots.get(1));
    ContactService.ContactUpsertResult second =
        contactService.upsertPrimaryContact(snapshots.get(1), secondCustomer.customerRef());

    assertThat(first.created()).isTrue();
    assertThat(second.created()).isFalse();
    assertThat(first.contactRef().internalId()).isEqualTo("305");
    assertThat(second.contactRef().internalId()).isEqualTo(first.contactRef().internalId());
    assertThat(second.contactRef().customerInternalId()).isEqualTo(firstCustomer.customerRef().internalId());
    assertThat(second.contact().customer().internalId()).isEqualTo(secondCustomer.customerRef().internalId());
    assertThat(second.contact().phone()).isEqualTo("+66-555-0199");
    assertThat(second.contact().role()).isEqualTo("Warranty owner");
    assertThat(contactLookup.countContacts()).isEqualTo(1);
  }

  @Test
  void upsertContactRejectsSharedEntityExternalIdCollisions() {
    InMemoryEntityStore store = new InMemoryEntityStore();
    CustomerLookup customerLookup = new CustomerLookup(store);
    ContactLookup contactLookup = new ContactLookup(store);
    CustomerService customerService = new CustomerService(store, customerLookup);
    ContactService contactService = new ContactService(store, contactLookup);

    ChannelAccountSnapshot customerSnapshot = ChannelAccountSnapshot.storefrontTheo();
    customerService.upsertCustomer(customerSnapshot);

    ChannelAccountSnapshot badContactSnapshot = new ChannelAccountSnapshot(
        "support_portal",
        "customer_theo_tran",
        "customer_theo_tran",
        "Theo Tran",
        "theo@ai-pets.example",
        "+66-555-0101",
        "Theo",
        "Tran",
        "theo@ai-pets.example",
        "+66-555-0101",
        "Support contact");

    assertThatThrownBy(() -> contactService.upsertPrimaryContact(
        badContactSnapshot,
        customerLookup.findByExternalId("customer_theo_tran").orElseThrow().toRef()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("shared entity external ID space");

    assertThat(contactLookup.countContacts()).isEqualTo(0);
  }

  private static List<ChannelAccountSnapshot> loadSnapshots() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    try (InputStream stream = ContactServiceTest.class.getResourceAsStream("/theo-channel-snapshots.json")) {
      assertThat(stream).isNotNull();
      return objectMapper.readValue(stream, new TypeReference<>() { });
    }
  }
}
