# Designing NetSuite Integrations Code Index

This repository contains the chapter-scoped Java example projects for the book [Designing NetSuite Integrations](https://www.amazon.com/dp/B07HRDRZX2) (A practical guide to REST, SuiteQL, and workflow-first architecture). Each chapter module is intentionally self-contained, so readers can run one small workflow at a time without depending on a giant shared framework.

The projects focus on business-shaped NetSuite integration lessons: stable external IDs, workflow-first modeling, separate read and write paths, and observable chapter-local demos that stay close to the manuscript.

## Requirements

- Java 17
- Maven 3.9+ or the included Maven Wrapper for most chapter modules
- A shell environment that can run `./mvnw` on Unix-like systems

Most chapter projects can be tested with `./mvnw test`. The `sample` project currently has a broken wrapper checkout in this repo, so use `mvn test` there unless its `.mvn/wrapper` files are restored.

## How to test

Run one chapter locally:

```bash
cd ch03-hello-netsuite-your-first-small-integration
./mvnw test
```

Run all chapter modules one by one from the repo root:

```bash
for dir in ch0* ch1*; do
  (cd "$dir" && ./mvnw test)
done
```

## Chapter modules

- [ch03-hello-netsuite-your-first-small-integration](./ch03-hello-netsuite-your-first-small-integration)
  The opening slice accepts one small product-interest signup, assigns a stable external ID, writes it through a record client, and reads it back through a separate query client. It establishes the core posture for the rest of the book: business language first, transport details later.

- [ch04-learning-to-see-netsuite-as-records-and-states](./ch04-learning-to-see-netsuite-as-records-and-states)
  This module turns AI-Pets business moments into NetSuite record vocabulary and state transitions. It is less about HTTP calls and more about learning what each record means in the workflow.

- [ch05-ai-pets-commercial-lifecycle](./ch05-ai-pets-commercial-lifecycle)
  This chapter sketches the full customer journey from campaign to refund as one readable business timeline. It helps readers see how continuity matters across records before diving into deeper service slices.

- [ch06-customers-and-contacts-the-first-place-integrations-go-wrong](./ch06-customers-and-contacts-the-first-place-integrations-go-wrong)
  This project shows how to resolve the customer first, then upsert contacts without duplicating entity identity. It makes the customer/contact split explicit and keeps their external IDs intentionally separate.

- [ch07-sales-orders-where-intent-becomes-a-business-record](./ch07-sales-orders-where-intent-becomes-a-business-record)
  This module accepts a business-shaped order command and turns it into one durable sales order. It emphasizes that an order is a commitment record, not just a payload that happens to include line items.

- [ch08-invoices-payments-and-the-order-to-cash-path](./ch08-invoices-payments-and-the-order-to-cash-path)
  This chapter covers invoice creation and payment application as different business actions. It demonstrates partial payment, final settlement, and the read side needed to find invoices that still have money due.

- [ch09-fulfillment-and-operational-state](./ch09-fulfillment-and-operational-state)
  This module separates operational fulfillment truth from financial truth. It shows that shipment progress and invoice state often move on different tracks and need different projections.

- [ch10-query-reads-with-suiteql](./ch10-query-reads-with-suiteql)
  This project keeps read questions in a small query catalog and returns compact reconciliation rows instead of raw record dumps. It is the first strong example of why SuiteQL-style reads deserve their own seam.

- [ch11-campaigns-and-marketing-activity](./ch11-campaigns-and-marketing-activity)
  This chapter records campaign attribution and early marketing activity before a customer record even exists. It keeps writes and history reads separate so the pre-customer story stays observable.

- [ch12-leads-prospects-opportunities-and-qualification](./ch12-leads-prospects-opportunities-and-qualification)
  This module qualifies a CRM party without promoting it to customer too early. It treats lead and prospect as relationship stages and uses a separate opportunity record for the possible deal.

- [ch13-from-lead-to-customer-handoff](./ch13-from-lead-to-customer-handoff)
  This chapter preserves identity continuity as a qualified CRM party crosses into customer state. It focuses on handoff safety: keeping campaign, opportunity, contact, and customer context tied to one business identity.

- [ch14-support-cases-and-case-history](./ch14-support-cases-and-case-history)
  This project models support as a real post-sale workflow with its own current state and readable history. It treats open, assign, update, and close as business actions rather than generic status flips.

- [ch15-customer-returns-exchange-and-refund](./ch15-customer-returns-exchange-and-refund)
  This module shows the reverse workflow side of commerce through return authorization, exchange, and refund. It keeps the support case as the human story while operational and financial reverse records carry the workflow state.

- [ch16-record-transformations-in-real-workflows](./ch16-record-transformations-in-real-workflows)
  This chapter demonstrates why downstream records often start as transformations of existing records instead of manual reconstruction. It highlights derive-then-patch as the safer continuity-preserving pattern.

- [ch17-custom-fields-custom-records-and-metadata-discovery](./ch17-custom-fields-custom-records-and-metadata-discovery)
  This project shows why metadata discovery must come before mapping when custom fields and custom records are involved. It keeps account-specific schema decisions explicit instead of scattering hard-coded assumptions.

- [ch18-operational-hardening-async-idempotency-batch-and-logging](./ch18-operational-hardening-async-idempotency-batch-and-logging)
  This module moves from happy-path integration into operational safety: async workflows, idempotency, bounded retries, chunked batch work, and correlation-aware logging. It treats those concerns as business protection, not polish.

- [ch19-putting-the-ai-pets-lifecycle-together](./ch19-putting-the-ai-pets-lifecycle-together)
  This chapter composes earlier ideas into one readable end-to-end lifecycle recap without collapsing everything into a giant framework. The final output is a timeline view that proves continuity across the whole story.
