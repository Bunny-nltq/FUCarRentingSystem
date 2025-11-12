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
    private String accountName;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    private String role; // ADMIN / STAFF / CUSTOMER

    @Column(nullable = false)
    private Boolean isLocked = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // One-to-one with AccountProfile
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AccountProfile profile;

    // getters & setters
    // (generate via IDE)
}
