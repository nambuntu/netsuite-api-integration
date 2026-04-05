package com.github.nambuntu.netsuite.ch15.model;

import java.time.Instant;

public record ReturnProgressEvent(
    String supportCaseExternalId,
    int sequenceNumber,
    String eventType,
    String detail,
    Instant occurredAt) {
}
