package com.github.nambuntu.netsuite.ch15.service;

import com.github.nambuntu.netsuite.ch15.command.AuthorizeReturnCommand;
import com.github.nambuntu.netsuite.ch15.model.ReturnAuthorization;
import com.github.nambuntu.netsuite.ch15.model.ReturnAuthorizationStatus;
import com.github.nambuntu.netsuite.ch15.store.InMemoryReturnStore;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public final class ReturnAuthorizationService {

  private final InMemoryReturnStore store;

  public ReturnAuthorizationService(InMemoryReturnStore store) {
    this.store = Objects.requireNonNull(store, "store");
  }

  public ReturnAuthorization authorizeReturn(AuthorizeReturnCommand command) {
    Objects.requireNonNull(command, "command");
    ReturnAuthorization record = new ReturnAuthorization(
        command.returnAuthorizationExternalId(),
        command.request().supportCaseExternalId(),
        command.request().customerExternalId(),
        command.request().sourceSalesOrderExternalId(),
        command.disposition(),
        ReturnAuthorizationStatus.PENDING_RECEIPT,
        command.request().lines(),
        command.authorizedAt(),
        command.authorizedAt());
    store.saveReturnAuthorization(record);
    return record;
  }

  public ReturnAuthorization receiveReturn(String returnAuthorizationExternalId, Instant receivedAt) {
    ReturnAuthorization current = require(returnAuthorizationExternalId);
    ReturnAuthorization updated = new ReturnAuthorization(
        current.externalId(),
        current.supportCaseExternalId(),
        current.customerExternalId(),
        current.sourceSalesOrderExternalId(),
        current.disposition(),
        ReturnAuthorizationStatus.RECEIVED,
        current.lines(),
        current.authorizedAt(),
        receivedAt);
    store.saveReturnAuthorization(updated);
    return updated;
  }

  public ReturnAuthorization markExchanged(String returnAuthorizationExternalId, Instant completedAt) {
    ReturnAuthorization current = require(returnAuthorizationExternalId);
    ReturnAuthorization updated = new ReturnAuthorization(
        current.externalId(),
        current.supportCaseExternalId(),
        current.customerExternalId(),
        current.sourceSalesOrderExternalId(),
        current.disposition(),
        ReturnAuthorizationStatus.EXCHANGED,
        current.lines(),
        current.authorizedAt(),
        completedAt);
    store.saveReturnAuthorization(updated);
    return updated;
  }

  public ReturnAuthorization markRefunded(String returnAuthorizationExternalId, Instant completedAt) {
    ReturnAuthorization current = require(returnAuthorizationExternalId);
    ReturnAuthorization updated = new ReturnAuthorization(
        current.externalId(),
        current.supportCaseExternalId(),
        current.customerExternalId(),
        current.sourceSalesOrderExternalId(),
        current.disposition(),
        ReturnAuthorizationStatus.REFUNDED,
        current.lines(),
        current.authorizedAt(),
        completedAt);
    store.saveReturnAuthorization(updated);
    return updated;
  }

  public List<ReturnAuthorization> findBySupportCase(String supportCaseExternalId) {
    return store.findReturnAuthorizationsBySupportCase(supportCaseExternalId);
  }

  public ReturnAuthorization findByExternalId(String returnAuthorizationExternalId) {
    return require(returnAuthorizationExternalId);
  }

  private ReturnAuthorization require(String returnAuthorizationExternalId) {
    return store.findReturnAuthorization(returnAuthorizationExternalId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown return authorization: " + returnAuthorizationExternalId));
  }
}
