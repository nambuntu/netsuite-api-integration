package com.github.nambuntu.netsuite.ch17.model;

import java.util.Objects;

public record CustomFieldDefinition(String fieldId, String label, boolean required, String customRecordTypeId) {

  public CustomFieldDefinition {
    Objects.requireNonNull(fieldId, "fieldId");
    Objects.requireNonNull(label, "label");
  }

  public FieldDefinition asFieldDefinition() {
    return new FieldDefinition(fieldId, label, FieldKind.CUSTOM_BODY, required);
  }

  public boolean isCustomRecordBacked() {
    return customRecordTypeId != null && !customRecordTypeId.isBlank();
  }
}
