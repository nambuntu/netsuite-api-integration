package com.github.nambuntu.netsuite.ch16.model;

import java.util.List;

public record SourceSalesOrder(
    String externalId,
    String customerExternalId,
    List<SourceSalesOrderLine> lines) {
}
