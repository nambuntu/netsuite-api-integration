package com.github.nambuntu.netsuite.ch16;

import com.github.nambuntu.netsuite.ch16.fixture.TransformationFixtures;
import com.github.nambuntu.netsuite.ch16.gateway.MockTransformGateway;
import com.github.nambuntu.netsuite.ch16.model.DerivedInvoice;
import com.github.nambuntu.netsuite.ch16.model.TransformPatch;
import com.github.nambuntu.netsuite.ch16.service.DerivedRecordPreviewService;
import com.github.nambuntu.netsuite.ch16.service.InvoiceAssemblyComparisonService;
import com.github.nambuntu.netsuite.ch16.service.RecordTransformationService;
import com.github.nambuntu.netsuite.ch16.store.InMemoryTransformationStore;

public final class TransformationWorkflowDemo {

  private TransformationWorkflowDemo() {
  }

  public static void main(String[] args) {
    System.out.println(renderDemo());
  }

  public static String renderDemo() {
    InMemoryTransformationStore store = TransformationFixtures.seededStore();
    MockTransformGateway transformGateway = new MockTransformGateway(store);
    DerivedRecordPreviewService previewService = new DerivedRecordPreviewService(store, transformGateway);
    RecordTransformationService recordTransformationService =
        new RecordTransformationService(previewService, transformGateway);
    InvoiceAssemblyComparisonService invoiceAssemblyComparisonService =
        new InvoiceAssemblyComparisonService(store);

    TransformPatch patch = new TransformPatch(
        "Bill and ship from the chapter transform flow",
        "BANGKOK-1",
        "case-theo-color-mismatch");

    var invoiceResult = recordTransformationService.invoiceFromSalesOrder("so-theo-1001", patch);
    var fulfillmentResult = recordTransformationService.fulfillmentFromSalesOrder("so-theo-1001", patch);
    var returnFromSalesOrder = recordTransformationService.returnAuthorizationFromSalesOrder("so-theo-1001", patch);
    var returnFromFulfillment = recordTransformationService.returnAuthorizationFromFulfillment("if-theo-1001", patch);
    DerivedInvoice manualInvoice = invoiceAssemblyComparisonService.assembleInvoiceManually("so-theo-1001", patch);

    StringBuilder builder = new StringBuilder();
    builder.append("source order: so-theo-1001").append(System.lineSeparator())
        .append("derived invoice: ").append(invoiceResult.targetRecordExternalId()).append(System.lineSeparator())
        .append("same customer: ").append(invoiceResult.customerExternalId()).append(System.lineSeparator())
        .append("invoice source link: ").append(invoiceResult.derivedRecord().sourceSalesOrderExternalId()).append(System.lineSeparator())
        .append("derived from source lines: ")
        .append(invoiceResult.lineContinuityPreserved() ? "yes" : "no")
        .append(System.lineSeparator())
        .append("patched fields: ").append(String.join(", ", invoiceResult.patchedFields())).append(System.lineSeparator())
        .append("derived fulfillment: ").append(fulfillmentResult.targetRecordExternalId()).append(System.lineSeparator())
        .append("fulfillment source link: ").append(fulfillmentResult.derivedRecord().sourceSalesOrderExternalId()).append(System.lineSeparator())
        .append("return from sales order: ").append(returnFromSalesOrder.targetRecordExternalId()).append(System.lineSeparator())
        .append("return sales-order link: ").append(returnFromSalesOrder.derivedRecord().sourceRecordExternalId()).append(System.lineSeparator())
        .append("return from fulfillment: ").append(returnFromFulfillment.targetRecordExternalId()).append(System.lineSeparator())
        .append("return fulfillment link: ").append(returnFromFulfillment.derivedRecord().sourceRecordExternalId()).append(System.lineSeparator())
        .append("manual assembly warning: ")
        .append(manualContinuityWarning(manualInvoice));
    return builder.toString();
  }

  private static String manualContinuityWarning(DerivedInvoice manualInvoice) {
    boolean missingSourceLink = manualInvoice.sourceSalesOrderExternalId() == null;
    boolean missingLineLink = manualInvoice.lines().stream()
        .anyMatch(line -> line.sourceSalesOrderLineReference() == null);
    return missingSourceLink || missingLineLink
        ? "source linkage must be re-copied explicitly"
        : "manual assembly matched source continuity";
  }
}
