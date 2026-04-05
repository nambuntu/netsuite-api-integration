# Chapter 8 - Invoices, payments, and the order-to-cash path

This chapter project is a standalone Java example for one narrow lesson:

- create an invoice from a known sales order
- apply a partial payment and then a final payment
- keep payment application separate from invoice creation
- query only invoices that still have a positive amount due

The code is intentionally chapter-local. It does not depend on earlier chapter projects or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch08` for the chapter models, fixtures, store, services, and demo
- `src/test/java/com/github/nambuntu/netsuite/ch08` for the chapter tests

## Run the tests

```bash
./mvnw test
```

If this is the first wrapper run on a machine, Maven may download the wrapper jar and Maven distribution before executing.

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch08.OrderToCashDemo
```

## Expected output

```text
Created invoice inv-theo-1001 from sales order so-theo-1001
Initial amount due for customer-theo-tran: 289.00 EUR

Applied payment pay-theo-1001-part1 for 100.00 EUR
Invoice inv-theo-1001 status: PARTIALLY_PAID
Remaining amount due: 189.00 EUR

Open invoices for customer-theo-tran:
- inv-theo-1001 | createdFrom=so-theo-1001 | amountDue=189.00 EUR | status=PARTIALLY_PAID

Applied payment pay-theo-1001-part2 for 189.00 EUR
Invoice inv-theo-1001 status: PAID
Remaining amount due: 0.00 EUR

Open invoices for customer-theo-tran:
- none
```
