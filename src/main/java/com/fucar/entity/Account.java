package com.fucar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountID;

    @Column(nullable = false, unique = true, length = 100)
    private String email; // dùng để login

    @Column(nullable = false, length = 100)
    private String accountName; // tên hiển thị

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    private String role; // ADMIN / CUSTOMER / STAFF

    @Column(nullable = false)
    private Boolean isLocked = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters & setters
    public Integer getAccountID() { return accountID; }
    public void setAccountID(Integer accountID) { this.accountID = accountID; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
