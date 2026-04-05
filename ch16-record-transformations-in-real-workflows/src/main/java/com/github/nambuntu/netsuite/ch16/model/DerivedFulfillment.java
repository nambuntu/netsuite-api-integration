package com.github.nambuntu.netsuite.ch16.model;

import java.util.List;

public record DerivedFulfillment(
    String externalId,
    String sourceSalesOrderExternalId,
    String customerExternalId,
    List<DerivedFulfillmentLine> lines,
    String memo,
    String location,
    String supportCaseLink) {
}
