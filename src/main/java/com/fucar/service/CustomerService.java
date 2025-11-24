package com.fucar.service;

import com.fucar.entity.Account;
import com.fucar.entity.Customer;
import com.fucar.repository.CustomerRepository;

import java.util.List;

public class CustomerService {

    private final CustomerRepository repo = new CustomerRepository();

    // =====================================================
    // CRUD
    // =====================================================

    public void addCustomer(Customer customer) {
        repo.save(customer);
    }

    public void updateCustomer(Customer customer) {
        repo.update(customer);
    }

    public void deleteCustomer(int customerID) {
        repo.deleteById(customerID);
    }

    public Customer getCustomerById(int id) {
        return repo.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return repo.findAll();
    }

    // =====================================================
    // FIND BY ACCOUNT ID (Login)
    // =====================================================

    public Customer findByAccountId(int accountId) {
        return repo.findByAccountId(accountId);
    }

    // =====================================================
    // TẠO CUSTOMER MẶC ĐỊNH (nếu dùng register)
    // =====================================================

    public Customer createDefaultCustomer(Account account) {

        Customer c = new Customer();

        c.setAccount(account);
        c.setEmail(account.getEmail());
        c.setCustomerName(account.getAccountName());

        // Các trường NOT NULL
        c.setMobile("N/A");
        c.setPassword("123456");

        // Optional
        c.setIdentityCard("N/A");
        c.setLicenceNumber("N/A");
        c.setBirthday(null);
        c.setLicenceDate(null);

        repo.save(c);

        return c;
    }
}
