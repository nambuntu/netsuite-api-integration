# Chapter 11 - Campaigns and marketing activity

This chapter project is a standalone Java example for one narrow lesson:

- preserve campaign source attribution before customer creation
- record early response and follow-up activity in business language
- keep campaign writes separate from campaign history reads
- make the pre-customer story visible from one compact demo

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch11/model` for campaign and activity models
- `src/main/java/com/github/nambuntu/netsuite/ch11/command` for activity commands
- `src/main/java/com/github/nambuntu/netsuite/ch11/service` for the campaign and activity services
- `src/main/java/com/github/nambuntu/netsuite/ch11/query` for the small query catalog
- `src/main/java/com/github/nambuntu/netsuite/ch11/client/query` for tiny query primitives
- `src/main/java/com/github/nambuntu/netsuite/ch11/mock` for deterministic in-memory stores and query behavior
- `src/main/java/com/github/nambuntu/netsuite/ch11` for the runnable demo

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch11.CampaignActivityDemo
```

## Expected output

```text
Campaign: campaign-ai-pets-echo-owl-launch (EMAIL, ACTIVE)
History:
- 2026-04-01T09:00:00Z RESPONSE_CAPTURED lead-theo-tran requested updates
- 2026-04-01T10:00:00Z FOLLOW_UP_SCHEDULED sales-ops demo call scheduled
```
