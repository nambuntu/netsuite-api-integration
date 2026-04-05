package com.github.nambuntu.netsuite.ch16.model;

public record DerivedReturnLine(
    String sourceLineReference,
    String itemExternalId,
    int quantity) {
}
