# Chapter 5 - The AI-Pets commercial lifecycle

This chapter project is a standalone Java sketch for one teaching task:

- carry Theo Tran's recurring external IDs from campaign through refund
- show the lifecycle as a business timeline rather than isolated record examples
- keep the duplicate guard tiny and visible
- print one readable summary that later service chapters can grow into

The code is intentionally chapter-local. It does not depend on earlier chapter projects, HTTP clients, or a mock NetSuite store.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch05` for the lifecycle sketch types
- `src/test/java/com/github/nambuntu/netsuite/ch05` for the chapter tests

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch05.AiPetsCommercialLifecycleDemo
```

## Expected output

```text
AI-Pets commercial lifecycle for Theo Tran
campaign      campaign-ai-pets-launch     Ava launches the Nimbus Cat campaign
interest      lead-theo-tran              Theo responds and enters CRM tracking
customer      customer-theo-tran          Theo becomes a customer with preserved source attribution
order         so-theo-1001                Theo places a sales order
invoice       so-theo-1001                The order becomes billable
payment       so-theo-1001                Payment is received against the same order story
fulfillment   so-theo-1001                Jules ships the order
support       case-theo-color-mismatch    Mira opens a support case about the wrong colour
return        ra-theo-1001                The business authorizes a return
refund        refund-theo-1001            The money side is reversed
```
