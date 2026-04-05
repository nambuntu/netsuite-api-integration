package com.github.nambuntu.netsuite.ch06.model;

public record ChannelAccountSnapshot(
    String sourceChannel,
    String customerExternalId,
    String contactExternalId,
    String customerDisplayName,
    String customerEmail,
    String customerPhone,
    String contactFirstName,
    String contactLastName,
    String contactEmail,
    String contactPhone,
    String contactRole) {

  public ChannelAccountSnapshot {
    sourceChannel = requireText(sourceChannel, "sourceChannel");
    customerExternalId = requireText(customerExternalId, "customerExternalId");
    contactExternalId = requireText(contactExternalId, "contactExternalId");
    customerDisplayName = requireText(customerDisplayName, "customerDisplayName");
    customerEmail = requireText(customerEmail, "customerEmail");
    customerPhone = requireText(customerPhone, "customerPhone");
    contactFirstName = requireText(contactFirstName, "contactFirstName");
    contactLastName = requireText(contactLastName, "contactLastName");
    contactEmail = requireText(contactEmail, "contactEmail");
    contactPhone = requireText(contactPhone, "contactPhone");
    contactRole = requireText(contactRole, "contactRole");
  }

  public static ChannelAccountSnapshot storefrontTheo() {
    return new ChannelAccountSnapshot(
        "storefront",
        "customer_theo_tran",
        "contact_theo_tran_primary",
        "Theo Tran",
        "theo@ai-pets.example",
        "+66-555-0101",
        "Theo",
        "Tran",
        "theo@ai-pets.example",
        "+66-555-0101",
        "Primary buyer");
  }

  public static ChannelAccountSnapshot returningTheo() {
    return new ChannelAccountSnapshot(
        "warranty_portal",
        "customer_theo_tran",
        "contact_theo_tran_primary",
        "THEO TRAN",
        "theo@ai-pets.example",
        "+66-555-0199",
        "THEO",
        "TRAN",
        "theo@ai-pets.example",
        "+66-555-0199",
        "Warranty owner");
  }

  private static String requireText(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value;
  }
}
