# Chapter 9 - Fulfillment and operational state

This chapter project is a standalone Java example for one narrow lesson:

- keep fulfillment as its own operational event stream
- project operational state from order lines plus fulfillment history
- show that financial state can remain `INVOICED` while shipment is still pending or partial
- make partial fulfillment visible at line level

The code is intentionally chapter-local. It does not depend on a live NetSuite account, warehouse systems, or carrier APIs.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch09` for the chapter models, stores, workflow, projector, and demo
- `src/test/java/com/github/nambuntu/netsuite/ch09` for the chapter tests

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch09.FulfillmentStateDemo
```

## Expected output

```text
Order: so-theo-1001
Customer: customer-theo-tran
Financial state: INVOICED (invoice inv-theo-1001)
Operational state: PENDING_FULFILLMENT
Lines:
- Nimbus Cat body ........ ordered 1, fulfilled 0
- Accessory bundle ....... ordered 1, fulfilled 0
- Personality pack ....... ordered 1, fulfilled 0
Fulfillment events:
- none

After packed event
Financial state: INVOICED (invoice inv-theo-1001)
Operational state: PENDING_FULFILLMENT
Lines:
- Nimbus Cat body ........ ordered 1, fulfilled 0
- Accessory bundle ....... ordered 1, fulfilled 0
- Personality pack ....... ordered 1, fulfilled 0
Fulfillment events:
- if-theo-1001-0  PACKED   warehouse-check

After first shipment
Financial state: INVOICED (invoice inv-theo-1001)
Operational state: PARTIALLY_FULFILLED
Lines:
- Nimbus Cat body ........ ordered 1, fulfilled 1
- Accessory bundle ....... ordered 1, fulfilled 0
- Personality pack ....... ordered 1, fulfilled 1
Fulfillment events:
- if-theo-1001-0  PACKED   warehouse-check
- if-theo-1001-1  SHIPPED  express

After second shipment
Financial state: INVOICED (invoice inv-theo-1001)
Operational state: FULFILLED
Lines:
- Nimbus Cat body ........ ordered 1, fulfilled 1
- Accessory bundle ....... ordered 1, fulfilled 1
- Personality pack ....... ordered 1, fulfilled 1
Fulfillment events:
- if-theo-1001-0  PACKED   warehouse-check
- if-theo-1001-1  SHIPPED  express
- if-theo-1001-2  SHIPPED  standard
```
