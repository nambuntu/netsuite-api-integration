package com.github.nambuntu.netsuite.ch15;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch15.fixture.ReturnFixtures;
import com.github.nambuntu.netsuite.ch15.fixture.ReturnsFixtureLoader;
import com.github.nambuntu.netsuite.ch15.service.ReturnAuthorizationService;
import com.github.nambuntu.netsuite.ch15.service.ReturnTimelineService;
import com.github.nambuntu.netsuite.ch15.service.ReturnWorkflowService;
import com.github.nambuntu.netsuite.ch15.store.InMemoryReturnStore;
import org.junit.jupiter.api.Test;

class ReturnTimelineServiceTest {

  @Test
  void timelineIncludesBothBranchesInOrder() {
    ReturnFixtures fixture = new ReturnsFixtureLoader().loadDefaultFixture();
    InMemoryReturnStore store = new InMemoryReturnStore();
    store.seedFixture(fixture);
    ReturnAuthorizationService returnAuthorizationService = new ReturnAuthorizationService(store);
    ReturnWorkflowService workflowService = new ReturnWorkflowService(returnAuthorizationService, store);
    ReturnTimelineService timelineService = new ReturnTimelineService(store);

    workflowService.authorizeExchange(fixture.exchangeAuthorizeCommand());
    workflowService.receiveReturn(fixture.exchangeReturnAuthorizationExternalId(), fixture.exchangeReceivedAt());
    workflowService.completeExchange(fixture.completeExchangeCommand());
    workflowService.authorizeRefund(fixture.refundAuthorizeCommand());
    workflowService.receiveReturn(fixture.finalReturnAuthorizationExternalId(), fixture.finalReceivedAt());
    workflowService.completeRefund(fixture.completeRefundCommand());

    assertThat(timelineService.findTimelineBySupportCase("case-theo-color-mismatch"))
        .extracting(event -> event.eventType() + " " + event.detail())
        .containsExactly(
            "OPENED Theo reports wrong color and overly chatty personality pack",
            "RETURN_AUTH ra-theo-1001-exchange linkedTo=so-theo-1001 disposition=EXCHANGE",
            "RECEIVED ra-theo-1001-exchange",
            "REPLACEMENT_ORDER so-theo-1001-r1 created for quiet-pack Nimbus Cat",
            "REPLACEMENT_SHIP if-theo-1001-r1 shipped",
            "RETURN_AUTH ra-theo-1001-final linkedTo=so-theo-1001-r1 disposition=REFUND",
            "RECEIVED ra-theo-1001-final",
            "CREDIT_ISSUED cm-theo-1001-final amount=249.00",
            "CUSTOMER_REFUND refund-theo-1001-final amount=249.00",
            "CASE_CLOSED Theo refunded and case resolved");
  }
}
