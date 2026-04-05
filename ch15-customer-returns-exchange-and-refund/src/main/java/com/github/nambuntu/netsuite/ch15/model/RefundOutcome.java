package com.github.nambuntu.netsuite.ch15.model;

import java.math.BigDecimal;
import java.time.Instant;

public record RefundOutcome(
    String creditMemoExternalId,
    String customerRefundExternalId,
    BigDecimal amount,
    Instant completedAt) {
}
