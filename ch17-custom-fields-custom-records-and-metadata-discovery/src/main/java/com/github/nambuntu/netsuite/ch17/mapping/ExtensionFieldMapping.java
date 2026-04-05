package com.github.nambuntu.netsuite.ch17.mapping;

import java.util.Objects;

public record ExtensionFieldMapping(
    String logicalName,
    String fieldId,
    boolean required,
    String customRecordTypeId) {

  public ExtensionFieldMapping {
    Objects.requireNonNull(logicalName, "logicalName");
    Objects.requireNonNull(fieldId, "fieldId");
  }

  public boolean isCustomRecordBacked() {
    return customRecordTypeId != null && !customRecordTypeId.isBlank();
  }
}
