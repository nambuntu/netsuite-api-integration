package com.github.nambuntu.netsuite.ch06.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nambuntu.netsuite.ch06.model.ChannelAccountSnapshot;
import com.github.nambuntu.netsuite.ch06.store.InMemoryEntityStore;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;

class CustomerServiceTest {

  @Test
  void upsertCustomerUpdatesMutableFieldsWithoutCreatingDuplicates() throws IOException {
    InMemoryEntityStore store = new InMemoryEntityStore();
    CustomerLookup lookup = new CustomerLookup(store);
    CustomerService service = new CustomerService(store, lookup);
    List<ChannelAccountSnapshot> snapshots = loadSnapshots();

    CustomerService.CustomerUpsertResult first = service.upsertCustomer(snapshots.get(0));
    CustomerService.CustomerUpsertResult second = service.upsertCustomer(snapshots.get(1));

    assertThat(first.created()).isTrue();
    assertThat(second.created()).isFalse();
    assertThat(first.customerRef().internalId()).isEqualTo("201");
    assertThat(second.customerRef().internalId()).isEqualTo(first.customerRef().internalId());
    assertThat(second.customer().displayName()).isEqualTo("THEO TRAN");
    assertThat(second.customer().phone()).isEqualTo("+66-555-0199");
    assertThat(lookup.countCustomers()).isEqualTo(1);
  }

  private static List<ChannelAccountSnapshot> loadSnapshots() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    try (InputStream stream = CustomerServiceTest.class.getResourceAsStream("/theo-channel-snapshots.json")) {
      assertThat(stream).isNotNull();
      return objectMapper.readValue(stream, new TypeReference<>() { });
    }
  }
}
