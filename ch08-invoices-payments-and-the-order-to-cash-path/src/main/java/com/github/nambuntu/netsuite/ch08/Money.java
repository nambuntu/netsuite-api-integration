package com.github.nambuntu.netsuite.ch08;

import java.math.BigDecimal;
import java.math.RoundingMode;

final class Money {

  private Money() {
  }

  static BigDecimal normalize(BigDecimal amount, String fieldName) {
    if (amount == null) {
      throw new IllegalArgumentException(fieldName + " must not be null");
    }
    BigDecimal normalized = amount.setScale(2, RoundingMode.UNNECESSARY);
    if (normalized.signum() < 0) {
      throw new IllegalArgumentException(fieldName + " must not be negative");
    }
    return normalized;
  }

  static BigDecimal positive(BigDecimal amount, String fieldName) {
    BigDecimal normalized = normalize(amount, fieldName);
    if (normalized.signum() == 0) {
      throw new IllegalArgumentException(fieldName + " must be greater than zero");
    }
    return normalized;
  }

  static String format(BigDecimal amount, String currencyCode) {
    return normalize(amount, "amount").toPlainString() + " " + currencyCode;
  }
}
