package com.github.nambuntu.netsuite.ch17.model;

import java.util.Objects;

public record FieldDefinition(String fieldId, String label, FieldKind kind, boolean required) {

  public FieldDefinition {
    Objects.requireNonNull(fieldId, "fieldId");
    Objects.requireNonNull(label, "label");
    Objects.requireNonNull(kind, "kind");
  }
}
