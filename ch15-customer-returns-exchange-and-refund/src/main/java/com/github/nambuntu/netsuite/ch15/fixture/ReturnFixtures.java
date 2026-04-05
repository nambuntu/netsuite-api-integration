package com.github.nambuntu.netsuite.ch15.fixture;

import com.github.nambuntu.netsuite.ch15.command.AuthorizeReturnCommand;
import com.github.nambuntu.netsuite.ch15.command.CompleteExchangeCommand;
import com.github.nambuntu.netsuite.ch15.command.CompleteRefundCommand;
import com.github.nambuntu.netsuite.ch15.model.ReturnDisposition;
import com.github.nambuntu.netsuite.ch15.model.ReturnLine;
import com.github.nambuntu.netsuite.ch15.model.ReturnRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ReturnFixtures(
    String supportCaseExternalId,
    String customerExternalId,
    String supportSubject,
    String supportPriority,
    String supportAssignee,
    String originalSalesOrderExternalId,
    String originalFulfillmentExternalId,
    String originalOpenedComment,
    Instant caseOpenedAt,
    String exchangeReturnAuthorizationExternalId,
    Instant exchangeAuthorizedAt,
    Instant exchangeReceivedAt,
    Instant exchangeCompletedAt,
    String exchangeSupportComment,
    ReturnLine exchangeReturnLine,
    String replacementSalesOrderExternalId,
    String replacementFulfillmentExternalId,
    String replacementDescription,
    String finalReturnAuthorizationExternalId,
    Instant finalAuthorizedAt,
    Instant finalReceivedAt,
    Instant finalCompletedAt,
    String refundSupportComment,
    ReturnLine refundReturnLine,
    String creditMemoExternalId,
    String customerRefundExternalId,
    BigDecimal refundAmount,
    String refundResolutionComment) {

  public ReturnRequest exchangeReturnRequest() {
    return new ReturnRequest(
        supportCaseExternalId,
        customerExternalId,
        originalSalesOrderExternalId,
        List.of(exchangeReturnLine));
  }

  public ReturnRequest refundReturnRequest() {
    return new ReturnRequest(
        supportCaseExternalId,
        customerExternalId,
        replacementSalesOrderExternalId,
        List.of(refundReturnLine));
  }

  public AuthorizeReturnCommand exchangeAuthorizeCommand() {
    return new AuthorizeReturnCommand(
        exchangeReturnAuthorizationExternalId,
        exchangeReturnRequest(),
        ReturnDisposition.EXCHANGE,
        exchangeSupportComment,
        exchangeAuthorizedAt);
  }

  public AuthorizeReturnCommand refundAuthorizeCommand() {
    return new AuthorizeReturnCommand(
        finalReturnAuthorizationExternalId,
        refundReturnRequest(),
        ReturnDisposition.REFUND,
        refundSupportComment,
        finalAuthorizedAt);
  }

  public CompleteExchangeCommand completeExchangeCommand() {
    return new CompleteExchangeCommand(
        exchangeReturnAuthorizationExternalId,
        replacementSalesOrderExternalId,
        replacementFulfillmentExternalId,
        replacementDescription,
        exchangeCompletedAt);
  }

  public CompleteRefundCommand completeRefundCommand() {
    return new CompleteRefundCommand(
        finalReturnAuthorizationExternalId,
        creditMemoExternalId,
        customerRefundExternalId,
        refundAmount,
        refundResolutionComment,
        finalCompletedAt);
  }
}
