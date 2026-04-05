package com.github.nambuntu.netsuite.ch15.model;

public record ReturnWorkflowResult(
    String supportCaseExternalId,
    String supportCaseStatus,
    ReturnAuthorization returnAuthorization,
    ExchangeOutcome exchangeOutcome,
    RefundOutcome refundOutcome) {
}
