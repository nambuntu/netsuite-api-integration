# Chapter 13 - From lead to customer handoff

This chapter project is a standalone Java example for one narrow lesson:

- preserve identity continuity as Theo crosses from CRM-side qualification into customer state
- keep contact continuity and customer continuity tied to the same `partyKey`
- preserve campaign and opportunity references through the handoff
- prove the final joined story with a read-back continuity view

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Identity note

This chapter uses `partyKey` as the stable source key and keeps record-specific external IDs per record type. Normalized email is continuity evidence only, not the primary authority when it conflicts with `partyKey`.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch13/model` for chapter handoff models
- `src/main/java/com/github/nambuntu/netsuite/ch13/service` for identity, CRM, contact, customer, and continuity services
- `src/main/java/com/github/nambuntu/netsuite/ch13/store` for the chapter-local in-memory store
- `src/main/java/com/github/nambuntu/netsuite/ch13/workflow` for the handoff workflow
- `src/main/java/com/github/nambuntu/netsuite/ch13` for the runnable demo

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch13.LeadToCustomerHandoffDemo
```

## Expected output

```text
handoff: SUCCESS
partyKey: party-theo-tran
lead: lead-theo-tran
prospect: prospect-theo-tran
opportunity: opp-theo-tran-nimbus
contact: contact-theo-tran
customer: customer-theo-tran
continuity: OK
notes: campaign-ai-pets-echo-owl-launch -> opp-theo-tran-nimbus -> customer-theo-tran
```
