package com.fucar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    private String fullName;
    private String phone;
    private String address;

    @OneToOne
    @JoinColumn(name = "accountId")
    private Account account;

    public Customer() {}

    public Customer(String fullName, String phone, String address, Account account) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.account = account;
    }

    // GETTER - SETTER

    public Integer getCustomerId() {
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Account getAccount() {
        return account;
    }
}
