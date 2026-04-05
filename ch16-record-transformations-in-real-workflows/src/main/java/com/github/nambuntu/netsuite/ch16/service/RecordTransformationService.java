package com.github.nambuntu.netsuite.ch16.service;

import com.github.nambuntu.netsuite.ch16.gateway.MockTransformGateway;
import com.github.nambuntu.netsuite.ch16.model.DerivedFulfillment;
import com.github.nambuntu.netsuite.ch16.model.DerivedInvoice;
import com.github.nambuntu.netsuite.ch16.model.DerivedReturnAuthorization;
import com.github.nambuntu.netsuite.ch16.model.TransformPatch;
import com.github.nambuntu.netsuite.ch16.model.TransformationResult;
import java.util.Objects;

public final class RecordTransformationService {

  private final DerivedRecordPreviewService previewService;
  private final MockTransformGateway transformGateway;

  public RecordTransformationService(
      DerivedRecordPreviewService previewService,
      MockTransformGateway transformGateway) {
    this.previewService = Objects.requireNonNull(previewService, "previewService");
    this.transformGateway = Objects.requireNonNull(transformGateway, "transformGateway");
  }

  public TransformationResult<DerivedInvoice> invoiceFromSalesOrder(String salesOrderExternalId, TransformPatch patch) {
    DerivedInvoice preview = previewService.previewInvoiceFromSalesOrder(salesOrderExternalId);
    DerivedInvoice patched = new DerivedInvoice(
        preview.externalId(),
        preview.sourceSalesOrderExternalId(),
        preview.customerExternalId(),
        preview.lines(),
        patch.memo(),
        patch.location(),
        patch.supportCaseLink());
    transformGateway.saveInvoice(patched);
    return new TransformationResult<>(
        salesOrderExternalId,
        patched.externalId(),
        patched.customerExternalId(),
        patch.patchedFieldNames(),
        salesOrderExternalId.equals(patched.sourceSalesOrderExternalId()),
        patched.lines().stream().allMatch(line -> line.sourceSalesOrderLineReference() != null),
        patched);
  }

  public TransformationResult<DerivedFulfillment> fulfillmentFromSalesOrder(String salesOrderExternalId, TransformPatch patch) {
    DerivedFulfillment preview = previewService.previewFulfillmentFromSalesOrder(salesOrderExternalId);
    DerivedFulfillment patched = new DerivedFulfillment(
        preview.externalId(),
        preview.sourceSalesOrderExternalId(),
        preview.customerExternalId(),
        preview.lines(),
        patch.memo(),
        patch.location(),
        patch.supportCaseLink());
    transformGateway.saveFulfillment(patched);
    return new TransformationResult<>(
        salesOrderExternalId,
        patched.externalId(),
        patched.customerExternalId(),
        patch.patchedFieldNames(),
        salesOrderExternalId.equals(patched.sourceSalesOrderExternalId()),
        patched.lines().stream().allMatch(line -> line.sourceSalesOrderLineReference() != null),
        patched);
  }

  public TransformationResult<DerivedReturnAuthorization> returnAuthorizationFromSalesOrder(String salesOrderExternalId, TransformPatch patch) {
    DerivedReturnAuthorization preview = previewService.previewReturnAuthorizationFromSalesOrder(salesOrderExternalId);
    DerivedReturnAuthorization patched = new DerivedReturnAuthorization(
        preview.externalId(),
        preview.sourceKind(),
        preview.sourceRecordExternalId(),
        preview.sourceSalesOrderExternalId(),
        preview.customerExternalId(),
        preview.lines(),
        patch.memo(),
        patch.location(),
        patch.supportCaseLink());
    transformGateway.saveReturnAuthorization(patched);
    return new TransformationResult<>(
        salesOrderExternalId,
        patched.externalId(),
        patched.customerExternalId(),
        patch.patchedFieldNames(),
        salesOrderExternalId.equals(patched.sourceRecordExternalId()),
        patched.lines().stream().allMatch(line -> line.sourceLineReference() != null),
        patched);
  }

  public TransformationResult<DerivedReturnAuthorization> returnAuthorizationFromFulfillment(String fulfillmentExternalId, TransformPatch patch) {
    DerivedReturnAuthorization preview = previewService.previewReturnAuthorizationFromFulfillment(fulfillmentExternalId);
    DerivedReturnAuthorization patched = new DerivedReturnAuthorization(
        preview.externalId(),
        preview.sourceKind(),
        preview.sourceRecordExternalId(),
        preview.sourceSalesOrderExternalId(),
        preview.customerExternalId(),
        preview.lines(),
        patch.memo(),
        patch.location(),
        patch.supportCaseLink());
    transformGateway.saveReturnAuthorization(patched);
    return new TransformationResult<>(
        fulfillmentExternalId,
        patched.externalId(),
        patched.customerExternalId(),
        patch.patchedFieldNames(),
        fulfillmentExternalId.equals(patched.sourceRecordExternalId()),
        patched.lines().stream().allMatch(line -> line.sourceLineReference() != null),
        patched);
  }
}
