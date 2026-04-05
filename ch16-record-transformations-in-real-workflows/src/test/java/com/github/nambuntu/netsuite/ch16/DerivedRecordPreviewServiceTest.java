package com.github.nambuntu.netsuite.ch16;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.nambuntu.netsuite.ch16.fixture.TransformationFixtures;
import com.github.nambuntu.netsuite.ch16.gateway.MockTransformGateway;
import com.github.nambuntu.netsuite.ch16.service.DerivedRecordPreviewService;
import com.github.nambuntu.netsuite.ch16.store.InMemoryTransformationStore;
import org.junit.jupiter.api.Test;

class DerivedRecordPreviewServiceTest {

  @Test
  void previewExposesDerivedDefaultsBeforeSave() {
    InMemoryTransformationStore store = TransformationFixtures.seededStore();
    DerivedRecordPreviewService service = new DerivedRecordPreviewService(store, new MockTransformGateway(store));

    var invoicePreview = service.previewInvoiceFromSalesOrder("so-theo-1001");
    var fulfillmentPreview = service.previewFulfillmentFromSalesOrder("so-theo-1001");
    var returnFromSalesOrderPreview = service.previewReturnAuthorizationFromSalesOrder("so-theo-1001");
    var returnFromFulfillmentPreview = service.previewReturnAuthorizationFromFulfillment("if-theo-1001");

    assertThat(invoicePreview.externalId()).isEqualTo("inv-theo-1001");
    assertThat(invoicePreview.memo()).isNull();
    assertThat(fulfillmentPreview.externalId()).isEqualTo("if-theo-1001");
    assertThat(returnFromSalesOrderPreview.externalId()).isEqualTo("ra-theo-1001");
    assertThat(returnFromSalesOrderPreview.sourceRecordExternalId()).isEqualTo("so-theo-1001");
    assertThat(returnFromFulfillmentPreview.externalId()).isEqualTo("ra-theo-1001-from-fulfillment");
    assertThat(returnFromFulfillmentPreview.sourceRecordExternalId()).isEqualTo("if-theo-1001");
  }
}
