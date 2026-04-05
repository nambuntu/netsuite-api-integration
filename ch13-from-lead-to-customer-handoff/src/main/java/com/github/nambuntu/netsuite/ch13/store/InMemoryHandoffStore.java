package com.github.nambuntu.netsuite.ch13.store;

import com.github.nambuntu.netsuite.ch13.model.Contact;
import com.github.nambuntu.netsuite.ch13.model.Customer;
import com.github.nambuntu.netsuite.ch13.model.Lead;
import com.github.nambuntu.netsuite.ch13.model.Opportunity;
import com.github.nambuntu.netsuite.ch13.model.Prospect;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class InMemoryHandoffStore {

  private final Map<String, Lead> leadsByExternalId = new LinkedHashMap<>();
  private final Map<String, Prospect> prospectsByExternalId = new LinkedHashMap<>();
  private final Map<String, Opportunity> opportunitiesByExternalId = new LinkedHashMap<>();
  private final Map<String, Contact> contactsByExternalId = new LinkedHashMap<>();
  private final Map<String, Customer> customersByExternalId = new LinkedHashMap<>();

  public Lead upsertLead(Lead lead) {
    leadsByExternalId.put(lead.externalId(), lead);
    return lead;
  }

  public Prospect upsertProspect(Prospect prospect) {
    prospectsByExternalId.put(prospect.externalId(), prospect);
    return prospect;
  }

  public Opportunity upsertOpportunity(Opportunity opportunity) {
    opportunitiesByExternalId.put(opportunity.externalId(), opportunity);
    return opportunity;
  }

  public Contact upsertContact(Contact contact) {
    contactsByExternalId.put(contact.externalId(), contact);
    return contact;
  }

  public Customer upsertCustomer(Customer customer) {
    customersByExternalId.put(customer.externalId(), customer);
    return customer;
  }

  public Optional<Lead> findLeadByPartyKey(String partyKey) {
    return leadsByExternalId.values().stream()
        .filter(record -> record.partyKey().equals(partyKey))
        .findFirst();
  }

  public Optional<Prospect> findProspectByPartyKey(String partyKey) {
    return prospectsByExternalId.values().stream()
        .filter(record -> record.partyKey().equals(partyKey))
        .findFirst();
  }

  public Optional<Opportunity> findOpportunityByPartyKey(String partyKey) {
    return opportunitiesByExternalId.values().stream()
        .filter(record -> record.partyKey().equals(partyKey))
        .findFirst();
  }

  public Optional<Contact> findContactByPartyKey(String partyKey) {
    return contactsByExternalId.values().stream()
        .filter(record -> record.partyKey().equals(partyKey))
        .findFirst();
  }

  public Optional<Customer> findCustomerByPartyKey(String partyKey) {
    return customersByExternalId.values().stream()
        .filter(record -> record.partyKey().equals(partyKey))
        .findFirst();
  }

  public int countContacts() {
    return contactsByExternalId.size();
  }

  public int countCustomers() {
    return customersByExternalId.size();
  }
}
