package com.github.nambuntu.netsuite.ch03.model;

public record SyncedContactInterestResult(
    String externalId,
    String contactDisplayName,
    String email,
    String productCode,
    String campaignCode,
    String syncStatus) {
}
