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

    // ================= GETTERS =================

    // Method mà controller gọi: getCustomerID()
    public Integer getCustomerID() {
        return customerId;
    }

    public Integer getCustomerId() { // vẫn giữ theo Java convention
        return customerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Account getAccount() {
        return account;
    }

    // ================= SETTERS =================
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
