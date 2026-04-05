# Chapter 14 - Support cases and case history

This chapter project is a standalone Java example for one narrow lesson:

- a support case is a post-sale workflow record with its own current state
- current case state and readable history are different shapes
- opening, assigning, updating, and closing a case are business actions
- the support workflow preserves `customer-theo-tran` and `so-theo-1001`

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch14/model` for current-state and history models
- `src/main/java/com/github/nambuntu/netsuite/ch14/command` for support workflow commands
- `src/main/java/com/github/nambuntu/netsuite/ch14/service` for the main support service
- `src/main/java/com/github/nambuntu/netsuite/ch14/query` for the tiny history query catalog
- `src/main/java/com/github/nambuntu/netsuite/ch14/client/query` for the chapter-local query primitives
- `src/main/java/com/github/nambuntu/netsuite/ch14/mock` for in-memory state and history storage
- `src/main/java/com/github/nambuntu/netsuite/ch14` for the runnable demo

## Duplicate case creation

This chapter chooses explicit rejection for duplicate `openCase` calls. A support case external ID is treated as a stable business key, so attempting to open the same case twice raises an error instead of silently creating a second `OPENED` history event.

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch14.SupportCaseTimelineDemo
```

## Expected output

```text
Opened support case: case-theo-color-mismatch for customer-theo-tran order=so-theo-1001
Open cases:
 - case-theo-color-mismatch [OPEN] priority=HIGH assignedTo=unassigned

Updated support case: case-theo-color-mismatch assignedTo=jules-ops status=IN_PROGRESS
Closed support case: case-theo-color-mismatch status=CLOSED

Case timeline:
 - OPENED by mira-agent: Wrong color and too-chatty personality pack reported
 - ASSIGNED by mira-agent -> jules-ops: Routed to operations for shipment mismatch review
 - UPDATED by jules-ops: Confirmed the wrong finish and personality pack mismatch
 - CLOSED by mira-agent: Replacement arranged and customer notified
```
