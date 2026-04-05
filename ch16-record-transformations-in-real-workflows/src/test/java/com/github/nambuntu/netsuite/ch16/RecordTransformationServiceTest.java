package com.github.nambuntu.netsuite.ch16;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch16.fixture.TransformationFixtures;
import com.github.nambuntu.netsuite.ch16.gateway.MockTransformGateway;
import com.github.nambuntu.netsuite.ch16.model.TransformPatch;
import com.github.nambuntu.netsuite.ch16.service.DerivedRecordPreviewService;
import com.github.nambuntu.netsuite.ch16.service.RecordTransformationService;
import com.github.nambuntu.netsuite.ch16.store.InMemoryTransformationStore;
import org.junit.jupiter.api.Test;

class RecordTransformationServiceTest {

  private final TransformPatch patch = new TransformPatch(
      "Bill and ship from the chapter transform flow",
      "BANGKOK-1",
      "case-theo-color-mismatch");

  @Test
  void salesOrderToInvoicePreservesSourceAndCustomerContinuity() {
    RecordTransformationService service = service();

    var result = service.invoiceFromSalesOrder("so-theo-1001", patch);

    assertThat(result.sourceRecordExternalId()).isEqualTo("so-theo-1001");
    assertThat(result.targetRecordExternalId()).isEqualTo("inv-theo-1001");
    assertThat(result.customerExternalId()).isEqualTo("customer-theo-tran");
    assertThat(result.sourceLinkPreserved()).isTrue();
    assertThat(result.lineContinuityPreserved()).isTrue();
  }

  @Test
  void patchChangesOnlyTargetSpecificFields() {
    RecordTransformationService service = service();

    var result = service.invoiceFromSalesOrder("so-theo-1001", patch);

    assertThat(result.derivedRecord().memo()).isEqualTo("Bill and ship from the chapter transform flow");
    assertThat(result.derivedRecord().location()).isEqualTo("BANGKOK-1");
    assertThat(result.derivedRecord().supportCaseLink()).isEqualTo("case-theo-color-mismatch");
    assertThat(result.derivedRecord().sourceSalesOrderExternalId()).isEqualTo("so-theo-1001");
  }

  @Test
  void salesOrderToFulfillmentPreservesSelectedLineLinkage() {
    RecordTransformationService service = service();

    var result = service.fulfillmentFromSalesOrder("so-theo-1001", patch);

    assertThat(result.targetRecordExternalId()).isEqualTo("if-theo-1001");
    assertThat(result.derivedRecord().lines())
        .extracting(line -> line.sourceSalesOrderLineReference())
        .containsExactly("so-theo-1001-line-1", "so-theo-1001-line-2");
  }

  @Test
  void salesOrderToReturnAuthorizationPreservesCommercialContinuity() {
    RecordTransformationService service = service();

    var result = service.returnAuthorizationFromSalesOrder("so-theo-1001", patch);

    assertThat(result.targetRecordExternalId()).isEqualTo("ra-theo-1001");
    assertThat(result.derivedRecord().sourceRecordExternalId()).isEqualTo("so-theo-1001");
    assertThat(result.derivedRecord().sourceKind().name()).isEqualTo("SALES_ORDER");
  }

  @Test
  void fulfillmentToReturnAuthorizationPreservesOperationalContinuity() {
    RecordTransformationService service = service();

    var result = service.returnAuthorizationFromFulfillment("if-theo-1001", patch);

    assertThat(result.targetRecordExternalId()).isEqualTo("ra-theo-1001-from-fulfillment");
    assertThat(result.derivedRecord().sourceRecordExternalId()).isEqualTo("if-theo-1001");
    assertThat(result.derivedRecord().sourceSalesOrderExternalId()).isEqualTo("so-theo-1001");
    assertThat(result.derivedRecord().sourceKind().name()).isEqualTo("FULFILLMENT");
  }

  private static RecordTransformationService service() {
    InMemoryTransformationStore store = TransformationFixtures.seededStore();
    MockTransformGateway gateway = new MockTransformGateway(store);
    DerivedRecordPreviewService previewService = new DerivedRecordPreviewService(store, gateway);
    return new RecordTransformationService(previewService, gateway);
  }
}
