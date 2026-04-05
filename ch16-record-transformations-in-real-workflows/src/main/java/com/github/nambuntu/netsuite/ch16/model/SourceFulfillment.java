package com.github.nambuntu.netsuite.ch16.model;

import java.util.List;

public record SourceFulfillment(
    String externalId,
    String sourceSalesOrderExternalId,
    String customerExternalId,
    List<String> fulfilledSalesOrderLineReferences) {
}
