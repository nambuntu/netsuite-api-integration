package com.github.nambuntu.netsuite.ch16.model;

public record DerivedFulfillmentLine(
    String sourceSalesOrderLineReference,
    String itemExternalId,
    int quantity) {
}
