package com.github.nambuntu.netsuite.ch14.command;

import java.time.Instant;

public record AssignSupportCaseCommand(
    String supportCaseExternalId,
    String assignedTo,
    String author,
    String comment,
    Instant changedAt) {
}
