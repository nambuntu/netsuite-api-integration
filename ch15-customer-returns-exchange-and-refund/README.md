# Chapter 15 - Customer returns, exchange, and refund

This chapter project is a standalone Java example for one narrow lesson:

- a return authorization is a formal reverse-workflow record
- exchange and refund are separate branches under one support case
- the same support case preserves the human story while reverse records carry the operational and financial story
- a timeline read makes the whole reverse flow observable

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch15/model` for the return workflow models
- `src/main/java/com/github/nambuntu/netsuite/ch15/command` for the return workflow commands
- `src/main/java/com/github/nambuntu/netsuite/ch15/service` for authorization, workflow, and timeline services
- `src/main/java/com/github/nambuntu/netsuite/ch15/fixture` for the chapter-local fixture loader
- `src/main/java/com/github/nambuntu/netsuite/ch15/store` for the in-memory store
- `src/test/resources/fixtures/ch15-returns-fixture.json` for the deterministic Theo scenario

## Fixture note

The chapter uses one JSON fixture so the reverse workflow remains readable without hard-coding every event inside the demo class.

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch15.ReturnWorkflowDemo
```

## Expected output

```text
case-theo-color-mismatch
  01 OPENED            Theo reports wrong color and overly chatty personality pack
  02 RETURN_AUTH       ra-theo-1001-exchange linkedTo=so-theo-1001 disposition=EXCHANGE
  03 RECEIVED          ra-theo-1001-exchange
  04 REPLACEMENT_ORDER so-theo-1001-r1 created for quiet-pack Nimbus Cat
  05 REPLACEMENT_SHIP  if-theo-1001-r1 shipped
  06 RETURN_AUTH       ra-theo-1001-final linkedTo=so-theo-1001-r1 disposition=REFUND
  07 RECEIVED          ra-theo-1001-final
  08 CREDIT_ISSUED     cm-theo-1001-final amount=249.00
  09 CUSTOMER_REFUND   refund-theo-1001-final amount=249.00
  10 CASE_CLOSED       Theo refunded and case resolved
```
