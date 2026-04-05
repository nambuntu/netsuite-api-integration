package com.github.nambuntu.netsuite.ch15;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch15.fixture.ReturnFixtures;
import com.github.nambuntu.netsuite.ch15.fixture.ReturnsFixtureLoader;
import com.github.nambuntu.netsuite.ch15.model.ReturnAuthorizationStatus;
import com.github.nambuntu.netsuite.ch15.service.ReturnAuthorizationService;
import com.github.nambuntu.netsuite.ch15.store.InMemoryReturnStore;
import org.junit.jupiter.api.Test;

class ReturnAuthorizationServiceTest {

  @Test
  void receivedReturnChangesAuthorizationStatus() {
    ReturnFixtures fixture = new ReturnsFixtureLoader().loadDefaultFixture();
    InMemoryReturnStore store = new InMemoryReturnStore();
    store.seedFixture(fixture);
    ReturnAuthorizationService service = new ReturnAuthorizationService(store);

    service.authorizeReturn(fixture.exchangeAuthorizeCommand());
    var updated = service.receiveReturn(
        fixture.exchangeReturnAuthorizationExternalId(),
        fixture.exchangeReceivedAt());

    assertThat(updated.status()).isEqualTo(ReturnAuthorizationStatus.RECEIVED);
  }

  @Test
  void findBySupportCaseReturnsBothAuthorizationsForTheoCase() {
    ReturnFixtures fixture = new ReturnsFixtureLoader().loadDefaultFixture();
    InMemoryReturnStore store = new InMemoryReturnStore();
    store.seedFixture(fixture);
    ReturnAuthorizationService service = new ReturnAuthorizationService(store);

    service.authorizeReturn(fixture.exchangeAuthorizeCommand());
    service.authorizeReturn(fixture.refundAuthorizeCommand());

    assertThat(service.findBySupportCase("case-theo-color-mismatch"))
        .extracting(record -> record.externalId())
        .containsExactly("ra-theo-1001-exchange", "ra-theo-1001-final");
  }
}
