# Chapter 17 - Custom fields, custom records, and metadata discovery

This chapter project is a standalone Java example for one narrow lesson:

- discover record metadata before mapping fields
- separate stable baseline mappings from account-specific extensions
- validate required custom fields before the write step
- resolve a custom-record-backed attribution value instead of guessing account schema from memory

The code is intentionally chapter-local. It does not depend on `sample` at runtime or a live NetSuite account.

## Project layout

- `src/main/java/com/github/nambuntu/netsuite/ch17/model` for metadata, lookup, and payload models
- `src/main/java/com/github/nambuntu/netsuite/ch17/mapping` for baseline and extension mapping rules
- `src/main/java/com/github/nambuntu/netsuite/ch17/service` for discovery, validation, and mapping services
- `src/main/java/com/github/nambuntu/netsuite/ch17/mock` for the chapter-local metadata catalog and custom-record lookup
- `src/main/java/com/github/nambuntu/netsuite/ch17` for the runnable demo

## Why this is safer

This chapter keeps custom field IDs and custom-record references in explicit metadata and mapping classes rather than scattering hard-coded field names through unrelated services. That makes required extensions visible, testable, and easier to review with admins or sales operations before a write call fails in production.

## Run the tests

```bash
./mvnw test
```

## Run the demo

```bash
./mvnw -q exec:java -Dexec.mainClass=com.github.nambuntu.netsuite.ch17.MetadataDiscoveryDemo
```

## Expected output

```text
record: campaign
standard fields discovered: 5
custom fields discovered: 3
custom record types discovered: 1
required extension fields present: yes
resolved attribution reference: attr-web-launch
mapped outbound fields:
- externalId
- title
- status
- owner
- budget
- custbody_acquisition_source
- custbody_region_bucket
- custbody_reporting_quarter
```
