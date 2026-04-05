package com.github.nambuntu.netsuite.ch14.model;

import java.time.Instant;

public record CaseHistoryEntry(
    String externalId,
    String supportCaseExternalId,
    CaseEventType eventType,
    CaseStatus statusAfterEvent,
    String author,
    String assignedTo,
    String visibility,
    String comment,
    Instant changedAt) {
}
