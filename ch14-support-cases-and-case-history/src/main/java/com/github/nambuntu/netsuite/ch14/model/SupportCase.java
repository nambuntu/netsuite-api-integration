package com.github.nambuntu.netsuite.ch14.model;

import java.time.Instant;

public record SupportCase(
    String externalId,
    String customerExternalId,
    String salesOrderExternalId,
    String subject,
    CaseStatus status,
    CasePriority priority,
    String assignedTo,
    Instant openedAt,
    Instant lastUpdatedAt) {
}
