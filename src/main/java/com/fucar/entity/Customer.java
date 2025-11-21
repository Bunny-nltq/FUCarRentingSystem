package com.fucar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID")
    private Integer customerID;

    @Column(name = "CustomerName")
    private String customerName;

    @Column(name = "Mobile")
    private String mobile;

    @Column(name = "Birthday")
    private String birthday;

    @Column(name = "IdentityCard")
    private String identityCard;

    @Column(name = "LicenceNumber")
    private String licenceNumber;

    @Column(name = "LicenceDate")
    private String licenceDate;

    @Column(name = "Email")
    private String email;

    @Column(name = "Password")
    private String password;

    @OneToOne
    @JoinColumn(name = "AccountID", nullable = false)
    private Account account;

    public Customer() {}

    // GETTERS & SETTERS

    public Integer getCustomerID() { return customerID; }
    public void setCustomerID(Integer customerID) { this.customerID = customerID; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getIdentityCard() { return identityCard; }
    public void setIdentityCard(String identityCard) { this.identityCard = identityCard; }

    public String getLicenceNumber() { return licenceNumber; }
    public void setLicenceNumber(String licenceNumber) { this.licenceNumber = licenceNumber; }

    public String getLicenceDate() { return licenceDate; }
    public void setLicenceDate(String licenceDate) { this.licenceDate = licenceDate; }

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
        return identityCard; // TẠM dùng IdentityCard làm address
    }

    public void setAddress(String address) {
        this.identityCard = address;
    }
}
