package com.github.nambuntu.netsuite.ch15.service;

import com.github.nambuntu.netsuite.ch15.command.AuthorizeReturnCommand;
import com.github.nambuntu.netsuite.ch15.command.CompleteExchangeCommand;
import com.github.nambuntu.netsuite.ch15.command.CompleteRefundCommand;
import com.github.nambuntu.netsuite.ch15.model.ExchangeOutcome;
import com.github.nambuntu.netsuite.ch15.model.RefundOutcome;
import com.github.nambuntu.netsuite.ch15.model.ReturnAuthorization;
import com.github.nambuntu.netsuite.ch15.model.ReturnWorkflowResult;
import com.github.nambuntu.netsuite.ch15.store.InMemoryReturnStore;
import java.time.Instant;
import java.util.Objects;

public final class ReturnWorkflowService {

  private final ReturnAuthorizationService returnAuthorizationService;
  private final InMemoryReturnStore store;

  public ReturnWorkflowService(ReturnAuthorizationService returnAuthorizationService, InMemoryReturnStore store) {
    this.returnAuthorizationService = Objects.requireNonNull(returnAuthorizationService, "returnAuthorizationService");
    this.store = Objects.requireNonNull(store, "store");
  }

  public ReturnWorkflowResult authorizeExchange(AuthorizeReturnCommand command) {
    ReturnAuthorization record = returnAuthorizationService.authorizeReturn(command);
    store.appendEvent(
        record.supportCaseExternalId(),
        "RETURN_AUTH",
        record.externalId() + " linkedTo=" + record.sourceSalesOrderExternalId() + " disposition=" + record.disposition(),
        command.authorizedAt());
    return result(record.supportCaseExternalId(), record, null, null);
  }

  public ReturnWorkflowResult receiveReturn(String returnAuthorizationExternalId, Instant receivedAt) {
    ReturnAuthorization record = returnAuthorizationService.receiveReturn(returnAuthorizationExternalId, receivedAt);
    store.appendEvent(
        record.supportCaseExternalId(),
        "RECEIVED",
        record.externalId(),
        receivedAt);
    return result(record.supportCaseExternalId(), record, null, null);
  }

  public ReturnWorkflowResult completeExchange(CompleteExchangeCommand command) {
    ReturnAuthorization record = returnAuthorizationService.markExchanged(
        command.returnAuthorizationExternalId(),
        command.completedAt());
    ExchangeOutcome outcome = new ExchangeOutcome(
        command.replacementSalesOrderExternalId(),
        command.replacementFulfillmentExternalId(),
        command.replacementDescription(),
        command.completedAt());
    store.saveExchangeOutcome(command.returnAuthorizationExternalId(), outcome);
    store.appendEvent(
        record.supportCaseExternalId(),
        "REPLACEMENT_ORDER",
        outcome.replacementSalesOrderExternalId() + " created for " + outcome.description(),
        command.completedAt());
    store.appendEvent(
        record.supportCaseExternalId(),
        "REPLACEMENT_SHIP",
        outcome.replacementFulfillmentExternalId() + " shipped",
        command.completedAt().plusSeconds(60));
    return result(record.supportCaseExternalId(), record, outcome, null);
  }

  public ReturnWorkflowResult authorizeRefund(AuthorizeReturnCommand command) {
    ReturnAuthorization record = returnAuthorizationService.authorizeReturn(command);
    store.appendEvent(
        record.supportCaseExternalId(),
        "RETURN_AUTH",
        record.externalId() + " linkedTo=" + record.sourceSalesOrderExternalId() + " disposition=" + record.disposition(),
        command.authorizedAt());
    return result(record.supportCaseExternalId(), record, null, null);
  }

  public ReturnWorkflowResult completeRefund(CompleteRefundCommand command) {
    ReturnAuthorization record = returnAuthorizationService.markRefunded(
        command.returnAuthorizationExternalId(),
        command.completedAt());
    RefundOutcome outcome = new RefundOutcome(
        command.creditMemoExternalId(),
        command.customerRefundExternalId(),
        command.amount(),
        command.completedAt());
    store.saveRefundOutcome(command.returnAuthorizationExternalId(), outcome);
    store.appendEvent(
        record.supportCaseExternalId(),
        "CREDIT_ISSUED",
        outcome.creditMemoExternalId() + " amount=" + outcome.amount(),
        command.completedAt());
    store.appendEvent(
        record.supportCaseExternalId(),
        "CUSTOMER_REFUND",
        outcome.customerRefundExternalId() + " amount=" + outcome.amount(),
        command.completedAt().plusSeconds(60));
    store.closeSupportCase(record.supportCaseExternalId(), command.completedAt().plusSeconds(120));
    store.appendEvent(
        record.supportCaseExternalId(),
        "CASE_CLOSED",
        command.resolutionComment(),
        command.completedAt().plusSeconds(120));
    return result(record.supportCaseExternalId(), record, null, outcome);
  }

  private ReturnWorkflowResult result(
      String supportCaseExternalId,
      ReturnAuthorization record,
      ExchangeOutcome exchangeOutcome,
      RefundOutcome refundOutcome) {
    return new ReturnWorkflowResult(
        supportCaseExternalId,
        store.supportCaseStatus(supportCaseExternalId),
        record,
        exchangeOutcome,
        refundOutcome);
  }
}
