package com.github.nambuntu.netsuite.ch15;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch15.fixture.ReturnFixtures;
import com.github.nambuntu.netsuite.ch15.fixture.ReturnsFixtureLoader;
import com.github.nambuntu.netsuite.ch15.service.ReturnAuthorizationService;
import com.github.nambuntu.netsuite.ch15.service.ReturnWorkflowService;
import com.github.nambuntu.netsuite.ch15.store.InMemoryReturnStore;
import org.junit.jupiter.api.Test;

class ReturnWorkflowServiceTest {

  @Test
  void acceptedReturnCreatesLinkedAuthorizationAndCaseUpdate() {
    Scenario scenario = Scenario.create();

    var result = scenario.workflowService.authorizeExchange(scenario.fixture.exchangeAuthorizeCommand());

    assertThat(result.returnAuthorization().externalId()).isEqualTo("ra-theo-1001-exchange");
    assertThat(result.returnAuthorization().sourceSalesOrderExternalId()).isEqualTo("so-theo-1001");
    assertThat(result.returnAuthorization().supportCaseExternalId()).isEqualTo("case-theo-color-mismatch");
    assertThat(scenario.store.timeline("case-theo-color-mismatch"))
        .extracting(event -> event.eventType() + " " + event.detail())
        .containsExactly(
            "OPENED Theo reports wrong color and overly chatty personality pack",
            "RETURN_AUTH ra-theo-1001-exchange linkedTo=so-theo-1001 disposition=EXCHANGE");
  }

  @Test
  void exchangePathCreatesReplacementOrderInsteadOfMutatingOriginalOrder() {
    Scenario scenario = Scenario.create();

    scenario.workflowService.authorizeExchange(scenario.fixture.exchangeAuthorizeCommand());
    scenario.workflowService.receiveReturn(
        scenario.fixture.exchangeReturnAuthorizationExternalId(),
        scenario.fixture.exchangeReceivedAt());
    var result = scenario.workflowService.completeExchange(scenario.fixture.completeExchangeCommand());

    assertThat(result.exchangeOutcome()).isNotNull();
    assertThat(result.exchangeOutcome().replacementSalesOrderExternalId()).isEqualTo("so-theo-1001-r1");
    assertThat(result.exchangeOutcome().replacementFulfillmentExternalId()).isEqualTo("if-theo-1001-r1");
    assertThat(result.returnAuthorization().sourceSalesOrderExternalId()).isEqualTo("so-theo-1001");
  }

  @Test
  void finalRefundCreatesCreditAndRefundAndClosesCase() {
    Scenario scenario = Scenario.create();

    scenario.workflowService.authorizeExchange(scenario.fixture.exchangeAuthorizeCommand());
    scenario.workflowService.receiveReturn(
        scenario.fixture.exchangeReturnAuthorizationExternalId(),
        scenario.fixture.exchangeReceivedAt());
    scenario.workflowService.completeExchange(scenario.fixture.completeExchangeCommand());
    scenario.workflowService.authorizeRefund(scenario.fixture.refundAuthorizeCommand());
    scenario.workflowService.receiveReturn(
        scenario.fixture.finalReturnAuthorizationExternalId(),
        scenario.fixture.finalReceivedAt());
    var result = scenario.workflowService.completeRefund(scenario.fixture.completeRefundCommand());

    assertThat(result.supportCaseStatus()).isEqualTo("CLOSED");
    assertThat(result.refundOutcome()).isNotNull();
    assertThat(result.refundOutcome().creditMemoExternalId()).isEqualTo("cm-theo-1001-final");
    assertThat(result.refundOutcome().customerRefundExternalId()).isEqualTo("refund-theo-1001-final");
    assertThat(result.refundOutcome().amount()).hasToString("249.00");
    assertThat(result.returnAuthorization().supportCaseExternalId()).isEqualTo("case-theo-color-mismatch");
  }

  private record Scenario(
      ReturnFixtures fixture,
      InMemoryReturnStore store,
      ReturnWorkflowService workflowService) {

    private static Scenario create() {
      ReturnFixtures fixture = new ReturnsFixtureLoader().loadDefaultFixture();
      InMemoryReturnStore store = new InMemoryReturnStore();
      store.seedFixture(fixture);
      ReturnAuthorizationService returnAuthorizationService = new ReturnAuthorizationService(store);
      ReturnWorkflowService workflowService = new ReturnWorkflowService(returnAuthorizationService, store);
      return new Scenario(fixture, store, workflowService);
    }
  }
}
