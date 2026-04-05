package com.github.nambuntu.netsuite.ch17.service;

import com.github.nambuntu.netsuite.ch17.mapping.ExtensionFieldMapping;
import com.github.nambuntu.netsuite.ch17.mapping.FieldMappingRegistry;
import com.github.nambuntu.netsuite.ch17.model.CampaignAttributionInput;
import java.util.Objects;

public final class RequiredFieldValidator {

  public void validate(CampaignAttributionInput input, FieldMappingRegistry registry) {
    Objects.requireNonNull(input, "input");
    Objects.requireNonNull(registry, "registry");

    ExtensionFieldMapping acquisitionSource = registry.extension("acquisitionSource");
    if (acquisitionSource.required()
        && (input.acquisitionSourceKey() == null || input.acquisitionSourceKey().isBlank())) {
      throw new MetadataValidationException(
          "Missing required custom field before write: " + acquisitionSource.fieldId());
    }
  }
}
