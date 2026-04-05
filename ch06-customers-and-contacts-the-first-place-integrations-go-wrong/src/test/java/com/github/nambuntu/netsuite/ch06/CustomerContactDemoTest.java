package com.github.nambuntu.netsuite.ch06;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class CustomerContactDemoTest {

  @Test
  void demoLinesShowCreateThenUpdateWithStableIds() {
    assertThat(CustomerContactDemo.demoLines()).containsExactlyElementsOf(List.of(
        "first sync  -> customer created  internalId=201  externalId=customer_theo_tran",
        "first sync  -> contact created  internalId=305  externalId=contact_theo_tran_primary  company=201",
        "second sync -> customer updated  internalId=201  externalId=customer_theo_tran",
        "second sync -> contact updated  internalId=305  externalId=contact_theo_tran_primary  company=201",
        "summary     -> customers=1 contacts=1 duplicateAvoidance=OK"));
  }
}
