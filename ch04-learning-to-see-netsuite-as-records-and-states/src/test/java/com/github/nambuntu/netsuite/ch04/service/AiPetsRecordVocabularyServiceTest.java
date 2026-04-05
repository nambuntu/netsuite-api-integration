package com.github.nambuntu.netsuite.ch04.service;

import com.github.nambuntu.netsuite.ch04.model.BusinessMoment;
import com.github.nambuntu.netsuite.ch04.model.BusinessStateMeaning;
import com.github.nambuntu.netsuite.ch04.model.IntegrationConcern;
import com.github.nambuntu.netsuite.ch04.model.NetSuiteRecordTypeName;
import com.github.nambuntu.netsuite.ch04.model.RecordVocabularyEntry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AiPetsRecordVocabularyServiceTest {

  private final AiPetsRecordVocabularyService service = new AiPetsRecordVocabularyService();

  @Test
  void placedOrderMapsToSalesOrderCommitmentStateAndWriteConcern() {
    RecordVocabularyEntry entry = service.describe(BusinessMoment.PLACED_ORDER);

    assertThat(entry.recordType()).isEqualTo(NetSuiteRecordTypeName.SALES_ORDER);
    assertThat(entry.stateMeaning()).isEqualTo(BusinessStateMeaning.COMMITMENT_STATE);
    assertThat(entry.integrationConcern()).isEqualTo(IntegrationConcern.WRITE);
  }

  @Test
  void openedOpportunityIsPresentAndUsesPreOrderSalesState() {
    RecordVocabularyEntry entry = service.describe(BusinessMoment.OPENED_OPPORTUNITY);

    assertThat(entry.recordType()).isEqualTo(NetSuiteRecordTypeName.OPPORTUNITY);
    assertThat(entry.stateMeaning()).isEqualTo(BusinessStateMeaning.PRE_ORDER_SALES_STATE);
    assertThat(entry.integrationConcern()).isEqualTo(IntegrationConcern.WRITE_AND_READ);
  }

  @Test
  void invoiceGuidancePrefersStableExternalIdentityOverDisplayedNumbers() {
    RecordVocabularyEntry entry = service.describe(BusinessMoment.BILLED_ORDER);

    assertThat(entry.identifierGuidance().summary())
        .contains("externalId like inv-theo-1001")
        .contains("source sales order linkage")
        .contains("displayed invoice numbers are useful labels, not the primary stable key");
  }

  @Test
  void mixedConcernMomentsStayMixedWhileSignupAndOrderStayWriteOriented() {
    assertThat(service.describe(BusinessMoment.SIGNED_UP_FOR_UPDATES).integrationConcern())
        .isEqualTo(IntegrationConcern.WRITE);
    assertThat(service.describe(BusinessMoment.PLACED_ORDER).integrationConcern())
        .isEqualTo(IntegrationConcern.WRITE);
    assertThat(service.describe(BusinessMoment.BILLED_ORDER).integrationConcern())
        .isEqualTo(IntegrationConcern.WRITE_AND_READ);
    assertThat(service.describe(BusinessMoment.FULFILLED_ORDER).integrationConcern())
        .isEqualTo(IntegrationConcern.WRITE_AND_READ);
    assertThat(service.describe(BusinessMoment.OPENED_SUPPORT_CASE).integrationConcern())
        .isEqualTo(IntegrationConcern.WRITE_AND_READ);
    assertThat(service.describe(BusinessMoment.APPROVED_RETURN).integrationConcern())
        .isEqualTo(IntegrationConcern.WRITE_AND_READ);
  }

  @Test
  void describeAllReturnsTheFullOrderedTeachingVocabulary() {
    List<String> lines = service.describeAll().stream()
        .map(RecordVocabularyEntry::teachingLine)
        .toList();

    assertThat(lines).containsExactly(
        "SIGNED_UP_FOR_UPDATES [Theo signs up for Nova Fox updates] -> contact -> relationship state -> write path -> key: prefer externalId like interest-theo-tran-nova-fox; support: campaign-ai-pets-launch and NOVA_FOX context",
        "OPENED_OPPORTUNITY [Theo asks about Nova Fox pre-order options] -> opportunity -> pre-order sales state -> write plus later read -> key: prefer externalId like opp-theo-nova-fox; support: customer or campaign continuity once the relationship deepens",
        "BECAME_PAYING_CUSTOMER [Theo becomes a billable AI-Pets customer] -> customer -> relationship state -> write plus later read -> key: prefer externalId like customer-theo-tran; support: internalId is a local handle inside NetSuite",
        "PLACED_ORDER [Theo places sales order so-theo-1001] -> salesOrder -> commitment state -> write path -> key: prefer externalId like so-theo-1001; support: customer-theo-tran continuity; note: displayed order numbers help humans, not primary record matching",
        "BILLED_ORDER [AI-Pets bills order so-theo-1001] -> invoice -> billable state -> write plus later read -> key: prefer externalId like inv-theo-1001; support: preserve source sales order linkage; note: displayed invoice numbers are useful labels, not the primary stable key",
        "RECORDED_PAYMENT [AI-Pets records payment for so-theo-1001] -> customerPayment -> settlement state -> write plus later read -> key: prefer externalId like pay-theo-1001; support: invoice and customer linkage explain what got settled",
        "FULFILLED_ORDER [Jules fulfills order so-theo-1001] -> itemFulfillment -> operational state -> write plus later read -> key: prefer externalId like fulfill-so-theo-1001; support: source order continuity matters more than shipping labels",
        "OPENED_SUPPORT_CASE [Mira opens case case-theo-color-mismatch] -> supportCase -> post-sale support state -> write plus later read -> key: prefer externalId like case-theo-color-mismatch; support: keep customer and order context attached",
        "APPROVED_RETURN [AI-Pets approves return ra-theo-1001] -> returnAuthorization -> controlled reverse-flow state -> write plus later read -> key: prefer externalId like ra-theo-1001; support: preserve source order and fulfillment continuity");
  }
}
