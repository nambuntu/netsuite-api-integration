package com.github.nambuntu.netsuite.ch15.command;

import java.math.BigDecimal;
import java.time.Instant;

public record CompleteRefundCommand(
    String returnAuthorizationExternalId,
    String creditMemoExternalId,
    String customerRefundExternalId,
    BigDecimal amount,
    String resolutionComment,
    Instant completedAt) {
}
