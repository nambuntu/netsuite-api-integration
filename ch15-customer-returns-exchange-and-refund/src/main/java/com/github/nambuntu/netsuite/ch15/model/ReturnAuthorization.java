package com.github.nambuntu.netsuite.ch15.model;

import java.time.Instant;
import java.util.List;

public record ReturnAuthorization(
    String externalId,
    String supportCaseExternalId,
    String customerExternalId,
    String sourceSalesOrderExternalId,
    ReturnDisposition disposition,
    ReturnAuthorizationStatus status,
    List<ReturnLine> lines,
    Instant authorizedAt,
    Instant lastUpdatedAt) {
}
