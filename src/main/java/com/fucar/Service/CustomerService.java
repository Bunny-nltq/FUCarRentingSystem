package com.fucar.service;

import com.fucar.entity.Customer;
import com.fucar.repository.CustomerRepository;

public class CustomerService {

	private final CustomerRepository repository = new CustomerRepository();

    public Customer findByAccountId(Integer accountId) {
        return repository.findByAccountId(accountId);
    }
}
