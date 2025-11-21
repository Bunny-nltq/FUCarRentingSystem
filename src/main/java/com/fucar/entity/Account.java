package com.fucar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID")
    private Integer accountId;

    @Column(name = "AccountName", nullable = false)
    private String accountName;

    @Column(nullable = false, unique = true)
    private String email;

    // ĐÚNG 100%: Khớp với table Account.PasswordHash
    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role; // ADMIN / CUSTOMER

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Customer customer;

    // =========================
    // Constructors
    // =========================

    public Account() {}

    public Account(String email, String passwordHash, String role, String accountName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.accountName = accountName;
    }

    // =========================
    // Getters
    // =========================

    public Integer getAccountId() {
        return accountId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public String getAccountName() {
        return accountName;
    }

    public Customer getCustomer() {
        return customer;
    }

    // =========================
    // Setters
    // =========================

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
