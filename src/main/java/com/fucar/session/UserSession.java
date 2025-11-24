package com.fucar.session;

import com.fucar.entity.Customer;

public class UserSession {

    private static UserSession instance;
    private Customer currentCustomer;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    public Customer getCustomer() {
        return currentCustomer;
    }

    public void clear() {
        currentCustomer = null;
    }
}
