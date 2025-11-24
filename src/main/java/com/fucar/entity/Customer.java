package com.fucar.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerID;

    @Column(name = "CustomerName", nullable = false)
    private String customerName;

    @Column(name = "Mobile", nullable = false)
    private String mobile;

    @Column(name = "Birthday")
    private LocalDate birthday;   // ← Changed String → LocalDate

    @Column(name = "IdentityCard")
    private String identityCard;

    @Column(name = "LicenceNumber")
    private String licenceNumber;

    @Column(name = "LicenceDate")
    private LocalDate licenceDate;  // ← Changed String → LocalDate

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Password", nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;

    public Customer() {}

    // ===================== GETTERS & SETTERS =====================

    public Integer getCustomerID() { return customerID; }
    public void setCustomerID(Integer customerID) { this.customerID = customerID; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public LocalDate getBirthday() { return birthday; }
    public void setBirthday(LocalDate birthday) { this.birthday = birthday; }

    public String getIdentityCard() { return identityCard; }
    public void setIdentityCard(String identityCard) { this.identityCard = identityCard; }

    public String getLicenceNumber() { return licenceNumber; }
    public void setLicenceNumber(String licenceNumber) { this.licenceNumber = licenceNumber; }

    public LocalDate getLicenceDate() { return licenceDate; }
    public void setLicenceDate(LocalDate licenceDate) { this.licenceDate = licenceDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    // ============== SUPPORT FOR PROFILE UI ==============

    public String getFullName() {
        return customerName;
    }

    public void setFullName(String fullName) {
        this.customerName = fullName;
    }

    public String getPhone() {
        return mobile;
    }

    public void setPhone(String phone) {
        this.mobile = phone;
    }

    public String getAddress() {
        return identityCard; // (temporary: using IdentityCard as address)
    }

    public void setAddress(String address) {
        this.identityCard = address;
    }
}
