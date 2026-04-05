package com.github.nambuntu.netsuite.ch17;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.nambuntu.netsuite.ch17.mapping.FieldMappingRegistry;
import com.github.nambuntu.netsuite.ch17.mock.InMemoryMetadataCatalog;
import com.github.nambuntu.netsuite.ch17.model.CampaignAttributionInput;
import com.github.nambuntu.netsuite.ch17.service.MetadataDiscoveryService;
import com.github.nambuntu.netsuite.ch17.service.MetadataValidationException;
import com.github.nambuntu.netsuite.ch17.service.RequiredFieldValidator;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class RequiredFieldValidatorTest {

  @Test
  void validateRejectsMissingRequiredAcquisitionSourceBeforeWrite() {
    InMemoryMetadataCatalog catalog = InMemoryMetadataCatalog.createDefault();
    MetadataDiscoveryService discoveryService = new MetadataDiscoveryService(catalog);
    FieldMappingRegistry registry = FieldMappingRegistry.fromMetadata(
        discoveryService.describeRecord("campaign"),
        discoveryService.describeCustomRecord("customrecord_campaign_attribution_policy"));
    RequiredFieldValidator validator = new RequiredFieldValidator();

    assertThatThrownBy(() -> validator.validate(
        new CampaignAttributionInput(
            "campaign-ai-pets-launch",
            "AI-Pets Spring Launch",
            "ACTIVE",
            "ava-ops",
            new BigDecimal("12500.00"),
            null,
            null,
            null),
        registry))
        .isInstanceOf(MetadataValidationException.class)
        .hasMessageContaining("custbody_acquisition_source");
  }

  @Test
  void validateAllowsOptionalRegionAndReportingFieldsToBeAbsent() {
    InMemoryMetadataCatalog catalog = InMemoryMetadataCatalog.createDefault();
    MetadataDiscoveryService discoveryService = new MetadataDiscoveryService(catalog);
    FieldMappingRegistry registry = FieldMappingRegistry.fromMetadata(
        discoveryService.describeRecord("campaign"),
        discoveryService.describeCustomRecord("customrecord_campaign_attribution_policy"));
    RequiredFieldValidator validator = new RequiredFieldValidator();

    assertThatCode(() -> validator.validate(
        new CampaignAttributionInput(
            "campaign-ai-pets-launch",
            "AI-Pets Spring Launch",
            "ACTIVE",
            "ava-ops",
            new BigDecimal("12500.00"),
            "web-launch",
            null,
            null),
        registry)).doesNotThrowAnyException();
  }
}
