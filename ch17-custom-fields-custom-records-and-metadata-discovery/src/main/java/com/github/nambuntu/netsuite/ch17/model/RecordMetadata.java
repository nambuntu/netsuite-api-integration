package com.github.nambuntu.netsuite.ch17.model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record RecordMetadata(
    String recordType,
    List<FieldDefinition> standardFields,
    List<CustomFieldDefinition> customFields) {

  public RecordMetadata {
    Objects.requireNonNull(recordType, "recordType");
    standardFields = List.copyOf(standardFields);
    customFields = List.copyOf(customFields);
  }

  public int standardFieldCount() {
    return standardFields.size();
  }

  public int customFieldCount() {
    return customFields.size();
  }

  public Optional<CustomFieldDefinition> findCustomField(String fieldId) {
    return customFields.stream().filter(field -> field.fieldId().equals(fieldId)).findFirst();
  }
}
