package com.github.nambuntu.netsuite.ch17;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MetadataDiscoveryDemoTest {

  @Test
  void renderDemoShowsDiscoveryCountsResolvedReferenceAndOutboundFields() {
    assertThat(MetadataDiscoveryDemo.renderDemo()).isEqualTo("""
        record: campaign
        standard fields discovered: 5
        custom fields discovered: 3
        custom record types discovered: 1
        required extension fields present: yes
        resolved attribution reference: attr-web-launch
        mapped outbound fields:
        - externalId
        - title
        - status
        - owner
        - budget
        - custbody_acquisition_source
        - custbody_region_bucket
        - custbody_reporting_quarter""");
  }
}
