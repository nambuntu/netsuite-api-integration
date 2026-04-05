package com.github.nambuntu.netsuite.ch14.command;

import com.github.nambuntu.netsuite.ch14.model.CasePriority;
import java.time.Instant;

public record CreateSupportCaseCommand(
    String supportCaseExternalId,
    String customerExternalId,
    String salesOrderExternalId,
    String subject,
    CasePriority priority,
    String author,
    String comment,
    Instant openedAt) {
}
