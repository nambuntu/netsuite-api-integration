package com.github.nambuntu.netsuite.ch04.service;

import com.github.nambuntu.netsuite.ch04.model.BusinessMoment;
import com.github.nambuntu.netsuite.ch04.model.BusinessStateMeaning;
import com.github.nambuntu.netsuite.ch04.model.IdentifierGuidance;
import com.github.nambuntu.netsuite.ch04.model.IntegrationConcern;
import com.github.nambuntu.netsuite.ch04.model.NetSuiteRecordTypeName;
import com.github.nambuntu.netsuite.ch04.model.RecordVocabularyEntry;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AiPetsRecordVocabularyService {

  private final Map<BusinessMoment, RecordVocabularyEntry> entriesByMoment;

  public AiPetsRecordVocabularyService() {
    EnumMap<BusinessMoment, RecordVocabularyEntry> entries = new EnumMap<>(BusinessMoment.class);
    entries.put(BusinessMoment.SIGNED_UP_FOR_UPDATES, new RecordVocabularyEntry(
        BusinessMoment.SIGNED_UP_FOR_UPDATES,
        NetSuiteRecordTypeName.CONTACT,
        BusinessStateMeaning.RELATIONSHIP_STATE,
        IntegrationConcern.WRITE,
        new IdentifierGuidance(
            "externalId like interest-theo-tran-nova-fox",
            "campaign-ai-pets-launch and NOVA_FOX context",
            null)));
    entries.put(BusinessMoment.OPENED_OPPORTUNITY, new RecordVocabularyEntry(
        BusinessMoment.OPENED_OPPORTUNITY,
        NetSuiteRecordTypeName.OPPORTUNITY,
        BusinessStateMeaning.PRE_ORDER_SALES_STATE,
        IntegrationConcern.WRITE_AND_READ,
        new IdentifierGuidance(
            "externalId like opp-theo-nova-fox",
            "customer or campaign continuity once the relationship deepens",
            null)));
    entries.put(BusinessMoment.BECAME_PAYING_CUSTOMER, new RecordVocabularyEntry(
        BusinessMoment.BECAME_PAYING_CUSTOMER,
        NetSuiteRecordTypeName.CUSTOMER,
        BusinessStateMeaning.RELATIONSHIP_STATE,
        IntegrationConcern.WRITE_AND_READ,
        new IdentifierGuidance(
            "externalId like customer-theo-tran",
            "internalId is a local handle inside NetSuite",
            null)));
    entries.put(BusinessMoment.PLACED_ORDER, new RecordVocabularyEntry(
        BusinessMoment.PLACED_ORDER,
        NetSuiteRecordTypeName.SALES_ORDER,
        BusinessStateMeaning.COMMITMENT_STATE,
        IntegrationConcern.WRITE,
        new IdentifierGuidance(
            "externalId like so-theo-1001",
            "customer-theo-tran continuity",
            "displayed order numbers help humans, not primary record matching")));
    entries.put(BusinessMoment.BILLED_ORDER, new RecordVocabularyEntry(
        BusinessMoment.BILLED_ORDER,
        NetSuiteRecordTypeName.INVOICE,
        BusinessStateMeaning.BILLABLE_STATE,
        IntegrationConcern.WRITE_AND_READ,
        new IdentifierGuidance(
            "externalId like inv-theo-1001",
            "preserve source sales order linkage",
            "displayed invoice numbers are useful labels, not the primary stable key")));
    entries.put(BusinessMoment.RECORDED_PAYMENT, new RecordVocabularyEntry(
        BusinessMoment.RECORDED_PAYMENT,
        NetSuiteRecordTypeName.CUSTOMER_PAYMENT,
        BusinessStateMeaning.SETTLEMENT_STATE,
        IntegrationConcern.WRITE_AND_READ,
        new IdentifierGuidance(
            "externalId like pay-theo-1001",
            "invoice and customer linkage explain what got settled",
            null)));
    entries.put(BusinessMoment.FULFILLED_ORDER, new RecordVocabularyEntry(
        BusinessMoment.FULFILLED_ORDER,
        NetSuiteRecordTypeName.ITEM_FULFILLMENT,
        BusinessStateMeaning.OPERATIONAL_STATE,
        IntegrationConcern.WRITE_AND_READ,
        new IdentifierGuidance(
            "externalId like fulfill-so-theo-1001",
            "source order continuity matters more than shipping labels",
            null)));
    entries.put(BusinessMoment.OPENED_SUPPORT_CASE, new RecordVocabularyEntry(
        BusinessMoment.OPENED_SUPPORT_CASE,
        NetSuiteRecordTypeName.SUPPORT_CASE,
        BusinessStateMeaning.POST_SALE_SUPPORT_STATE,
        IntegrationConcern.WRITE_AND_READ,
        new IdentifierGuidance(
            "externalId like case-theo-color-mismatch",
            "keep customer and order context attached",
            null)));
    entries.put(BusinessMoment.APPROVED_RETURN, new RecordVocabularyEntry(
        BusinessMoment.APPROVED_RETURN,
        NetSuiteRecordTypeName.RETURN_AUTHORIZATION,
        BusinessStateMeaning.CONTROLLED_REVERSE_FLOW_STATE,
        IntegrationConcern.WRITE_AND_READ,
        new IdentifierGuidance(
            "externalId like ra-theo-1001",
            "preserve source order and fulfillment continuity",
            null)));
    this.entriesByMoment = Map.copyOf(entries);
  }

  public RecordVocabularyEntry describe(BusinessMoment moment) {
    RecordVocabularyEntry entry = entriesByMoment.get(moment);
    if (entry == null) {
      throw new IllegalArgumentException("No record vocabulary entry defined for " + moment);
    }
    return entry;
  }

  public List<RecordVocabularyEntry> describeAll() {
    return Arrays.stream(BusinessMoment.values())
        .map(this::describe)
        .toList();
  }
}
