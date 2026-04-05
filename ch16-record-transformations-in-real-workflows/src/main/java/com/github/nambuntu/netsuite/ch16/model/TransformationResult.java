package com.github.nambuntu.netsuite.ch16.model;

import java.util.List;

public record TransformationResult<T>(
    String sourceRecordExternalId,
    String targetRecordExternalId,
    String customerExternalId,
    List<String> patchedFields,
    boolean sourceLinkPreserved,
    boolean lineContinuityPreserved,
    T derivedRecord) {
}
