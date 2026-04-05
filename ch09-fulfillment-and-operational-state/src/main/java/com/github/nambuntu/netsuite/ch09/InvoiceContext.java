package com.github.nambuntu.netsuite.ch09;

public record InvoiceContext(String invoiceExternalId, String financialState) {

  public InvoiceContext {
    invoiceExternalId = requireText(invoiceExternalId, "invoiceExternalId");
    financialState = requireText(financialState, "financialState");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
