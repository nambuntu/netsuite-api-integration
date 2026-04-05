package com.github.nambuntu.netsuite.ch04.model;

public record IdentifierGuidance(
    String preferredStableKey,
    String supportingLinkage,
    String caution) {

  public IdentifierGuidance {
    preferredStableKey = requireText(preferredStableKey, "preferredStableKey");
    supportingLinkage = normalize(supportingLinkage);
    caution = normalize(caution);
  }

  public String summary() {
    StringBuilder summary = new StringBuilder("prefer ").append(preferredStableKey);
    if (supportingLinkage != null) {
      summary.append("; support: ").append(supportingLinkage);
    }
    if (caution != null) {
      summary.append("; note: ").append(caution);
    }
    return summary.toString();
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value.trim();
  }

  private static String normalize(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    return value.trim();
  }
}
