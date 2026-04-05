package com.github.nambuntu.netsuite.ch16.model;

import java.util.List;

public record DerivedReturnAuthorization(
    String externalId,
    TransformSourceKind sourceKind,
    String sourceRecordExternalId,
    String sourceSalesOrderExternalId,
    String customerExternalId,
    List<DerivedReturnLine> lines,
    String memo,
    String location,
    String supportCaseLink) {
}
