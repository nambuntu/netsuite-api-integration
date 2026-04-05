package com.github.nambuntu.netsuite.ch14.command;

import java.time.Instant;

public record CloseSupportCaseCommand(
    String supportCaseExternalId,
    String author,
    String resolutionComment,
    Instant changedAt) {
}
