package com.github.nambuntu.netsuite.ch08;

public record CreateInvoiceFromSalesOrderCommand(String invoiceExternalId, String salesOrderExternalId) {

  public CreateInvoiceFromSalesOrderCommand {
    invoiceExternalId = requireText(invoiceExternalId, "invoiceExternalId");
    salesOrderExternalId = requireText(salesOrderExternalId, "salesOrderExternalId");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
