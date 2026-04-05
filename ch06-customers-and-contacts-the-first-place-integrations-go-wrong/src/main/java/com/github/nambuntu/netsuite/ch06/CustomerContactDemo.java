package com.github.nambuntu.netsuite.ch06;

import com.github.nambuntu.netsuite.ch06.model.ChannelAccountSnapshot;
import com.github.nambuntu.netsuite.ch06.service.ContactLookup;
import com.github.nambuntu.netsuite.ch06.service.ContactService;
import com.github.nambuntu.netsuite.ch06.service.CustomerLookup;
import com.github.nambuntu.netsuite.ch06.service.CustomerService;
import com.github.nambuntu.netsuite.ch06.store.InMemoryEntityStore;
import java.util.ArrayList;
import java.util.List;

public final class CustomerContactDemo {

  private CustomerContactDemo() {
  }

  public static void main(String[] args) {
    demoLines().forEach(System.out::println);
  }

  public static List<String> demoLines() {
    InMemoryEntityStore store = new InMemoryEntityStore();
    CustomerLookup customerLookup = new CustomerLookup(store);
    ContactLookup contactLookup = new ContactLookup(store);
    CustomerService customerService = new CustomerService(store, customerLookup);
    ContactService contactService = new ContactService(store, contactLookup);

    List<String> lines = new ArrayList<>();
    sync("first sync", ChannelAccountSnapshot.storefrontTheo(), customerService, contactService, lines);
    sync("second sync", ChannelAccountSnapshot.returningTheo(), customerService, contactService, lines);
    lines.add(String.format(
        "%-11s -> customers=%d contacts=%d duplicateAvoidance=%s",
        "summary",
        customerLookup.countCustomers(),
        contactLookup.countContacts(),
        duplicateAvoidanceStatus(customerLookup, contactLookup)));
    return List.copyOf(lines);
  }

  private static void sync(
      String label,
      ChannelAccountSnapshot snapshot,
      CustomerService customerService,
      ContactService contactService,
      List<String> lines) {

    CustomerService.CustomerUpsertResult customerResult = customerService.upsertCustomer(snapshot);
    lines.add(String.format(
        "%-11s -> customer %-7s  internalId=%s  externalId=%s",
        label,
        action(customerResult.created()),
        customerResult.customerRef().internalId(),
        customerResult.customerRef().externalId()));

    ContactService.ContactUpsertResult contactResult =
        contactService.upsertPrimaryContact(snapshot, customerResult.customerRef());
    lines.add(String.format(
        "%-11s -> contact %-7s  internalId=%s  externalId=%s  company=%s",
        label,
        action(contactResult.created()),
        contactResult.contactRef().internalId(),
        contactResult.contactRef().externalId(),
        contactResult.contactRef().customerInternalId()));
  }

  private static String action(boolean created) {
    return created ? "created" : "updated";
  }

  private static String duplicateAvoidanceStatus(CustomerLookup customerLookup, ContactLookup contactLookup) {
    return customerLookup.countCustomers() == 1 && contactLookup.countContacts() == 1 ? "OK" : "FAILED";
  }
}
