package com.github.nambuntu.netsuite.ch15.model;

import java.util.List;

public record ReturnRequest(
    String supportCaseExternalId,
    String customerExternalId,
    String sourceSalesOrderExternalId,
    List<ReturnLine> lines) {
}
