# Chapter 4 - Learning to see NetSuite as records and states

This chapter project is a standalone Java example for one teaching task:

- map recurring AI-Pets business moments to NetSuite record vocabulary
- make business-state meaning explicit
- classify whether the usual concern is write, read, or mixed
- show why external IDs remain the preferred stable integration key

The code is intentionally chapter-local. It does not depend on `sample`, shared enums, or HTTP-facing client code.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch04/model` for chapter enums and value objects
- `src/main/java/com/github/nambuntu/netsuite/ch04/service` for the vocabulary service
- `src/test/java/com/github/nambuntu/netsuite/ch04/service` for service behavior tests
- `src/test/java/com/github/nambuntu/netsuite/ch04` for the demo-output test

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch04.RecordVocabularyDemo
```

## Expected output

```text
AI-Pets record vocabulary
SIGNED_UP_FOR_UPDATES [Theo signs up for Nova Fox updates] -> contact -> relationship state -> write path -> key: prefer externalId like interest-theo-tran-nova-fox; support: campaign-ai-pets-launch and NOVA_FOX context
OPENED_OPPORTUNITY [Theo asks about Nova Fox pre-order options] -> opportunity -> pre-order sales state -> write plus later read -> key: prefer externalId like opp-theo-nova-fox; support: customer or campaign continuity once the relationship deepens
BECAME_PAYING_CUSTOMER [Theo becomes a billable AI-Pets customer] -> customer -> relationship state -> write plus later read -> key: prefer externalId like customer-theo-tran; support: internalId is a local handle inside NetSuite
PLACED_ORDER [Theo places sales order so-theo-1001] -> salesOrder -> commitment state -> write path -> key: prefer externalId like so-theo-1001; support: customer-theo-tran continuity; note: displayed order numbers help humans, not primary record matching
BILLED_ORDER [AI-Pets bills order so-theo-1001] -> invoice -> billable state -> write plus later read -> key: prefer externalId like inv-theo-1001; support: preserve source sales order linkage; note: displayed invoice numbers are useful labels, not the primary stable key
RECORDED_PAYMENT [AI-Pets records payment for so-theo-1001] -> customerPayment -> settlement state -> write plus later read -> key: prefer externalId like pay-theo-1001; support: invoice and customer linkage explain what got settled
FULFILLED_ORDER [Jules fulfills order so-theo-1001] -> itemFulfillment -> operational state -> write plus later read -> key: prefer externalId like fulfill-so-theo-1001; support: source order continuity matters more than shipping labels
OPENED_SUPPORT_CASE [Mira opens case case-theo-color-mismatch] -> supportCase -> post-sale support state -> write plus later read -> key: prefer externalId like case-theo-color-mismatch; support: keep customer and order context attached
APPROVED_RETURN [AI-Pets approves return ra-theo-1001] -> returnAuthorization -> controlled reverse-flow state -> write plus later read -> key: prefer externalId like ra-theo-1001; support: preserve source order and fulfillment continuity
```
