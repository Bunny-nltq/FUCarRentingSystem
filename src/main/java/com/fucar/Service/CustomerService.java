package com.fucar.service;

import com.fucar.entity.Customer;
import com.fucar.repository.CustomerRepository;

public class CustomerService {

	private final CustomerRepository repository = new CustomerRepository();

    public Customer findByAccountId(Integer accountId) {
        return repository.findByAccountId(accountId);
    }

	public Customer getCustomerById(int currentCustomerID) {
		// TODO Auto-generated method stub
		return null;
	}

	public Customer getAllCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addCustomer(Customer c) {
		// TODO Auto-generated method stub
		
	}

	public void updateCustomer(Customer c) {
		// TODO Auto-generated method stub
		
	}

	public void deleteCustomer(Integer customerID) {
		// TODO Auto-generated method stub
		
	}
}
