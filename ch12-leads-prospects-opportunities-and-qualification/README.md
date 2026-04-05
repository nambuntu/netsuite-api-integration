# Chapter 12 - Leads, prospects, opportunities, and qualification

This chapter project is a standalone Java example for one narrow lesson:

- qualify one CRM party without creating a customer too early
- treat lead and prospect as relationship stages on the same identity
- create one separate opportunity for the possible deal
- read open opportunities back in business terms

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## REST-first modeling note

The chapter uses one customer-backed CRM party abstraction whose stage moves from `LEAD` to `PROSPECT`, plus one separate opportunity record. It does not pretend lead and prospect are separate durable REST identities.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch12/model` for CRM and opportunity models
- `src/main/java/com/github/nambuntu/netsuite/ch12/command` for the qualification command
- `src/main/java/com/github/nambuntu/netsuite/ch12/service` for write and read services
- `src/main/java/com/github/nambuntu/netsuite/ch12/query` for the small opportunity query catalog
- `src/main/java/com/github/nambuntu/netsuite/ch12/client/query` for tiny query primitives
- `src/main/java/com/github/nambuntu/netsuite/ch12/mock` for deterministic in-memory stores and query behavior
- `src/main/java/com/github/nambuntu/netsuite/ch12` for the workflow and runnable demo

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch12.QualificationDemo
```

## Expected output

```text
CRM qualification result
party: crm-theo-tran
stage: LEAD -> PROSPECT
status: QUALIFIED -> IN_DISCUSSION
opportunity: opp-theo-nimbus-cat
amount: 1899.00
probability: 0.65
open opportunities for crm-theo-tran: 1
```
