package com.github.nambuntu.netsuite.ch16;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch16.fixture.TransformationFixtures;
import com.github.nambuntu.netsuite.ch16.model.TransformPatch;
import com.github.nambuntu.netsuite.ch16.service.InvoiceAssemblyComparisonService;
import org.junit.jupiter.api.Test;

class InvoiceAssemblyComparisonServiceTest {

  @Test
  void manualAssemblyLosesContinuityThatTransformCarriesForFree() {
    InvoiceAssemblyComparisonService service =
        new InvoiceAssemblyComparisonService(TransformationFixtures.seededStore());

    var manualInvoice = service.assembleInvoiceManually(
        "so-theo-1001",
        new TransformPatch("Bill and ship from the chapter transform flow", "BANGKOK-1", "case-theo-color-mismatch"));

    assertThat(manualInvoice.sourceSalesOrderExternalId()).isNull();
    assertThat(manualInvoice.supportCaseLink()).isNull();
    assertThat(manualInvoice.lines())
        .allMatch(line -> line.sourceSalesOrderLineReference() == null);
  }
}
