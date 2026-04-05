package com.github.nambuntu.netsuite.ch06.store;

import com.github.nambuntu.netsuite.ch06.model.Contact;
import com.github.nambuntu.netsuite.ch06.model.Customer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryEntityStore {

  private final Map<String, Customer> customersByExternalId = new LinkedHashMap<>();
  private final Map<String, Contact> contactsByExternalId = new LinkedHashMap<>();
  private int customerSequence = 200;
  private int contactSequence = 304;

  public String nextCustomerInternalId() {
    customerSequence += 1;
    return String.valueOf(customerSequence);
  }

  public String nextContactInternalId() {
    contactSequence += 1;
    return String.valueOf(contactSequence);
  }

  public Optional<Customer> findCustomerByExternalId(String externalId) {
    return Optional.ofNullable(customersByExternalId.get(externalId));
  }

  public Optional<Contact> findContactByExternalId(String externalId) {
    return Optional.ofNullable(contactsByExternalId.get(externalId));
  }

  public void assertCustomerExternalIdAvailable(String externalId) {
    if (contactsByExternalId.containsKey(externalId)) {
      throw new IllegalArgumentException(
          "customer external ID collides inside the shared entity external ID space: " + externalId);
    }
  }

  public void assertContactExternalIdAvailable(String externalId) {
    if (customersByExternalId.containsKey(externalId)) {
      throw new IllegalArgumentException(
          "contact external ID collides inside the shared entity external ID space: " + externalId);
    }
  }

  public void saveCustomer(Customer customer) {
    customersByExternalId.put(customer.externalId(), customer);
  }

  public void saveContact(Contact contact) {
    contactsByExternalId.put(contact.externalId(), contact);
  }

  public int customerCount() {
    return customersByExternalId.size();
  }

  public int contactCount() {
    return contactsByExternalId.size();
  }
}
