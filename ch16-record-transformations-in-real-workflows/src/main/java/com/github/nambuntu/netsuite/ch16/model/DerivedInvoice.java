package com.github.nambuntu.netsuite.ch16.model;

import java.util.List;

public record DerivedInvoice(
    String externalId,
    String sourceSalesOrderExternalId,
    String customerExternalId,
    List<DerivedInvoiceLine> lines,
    String memo,
    String location,
    String supportCaseLink) {
}
