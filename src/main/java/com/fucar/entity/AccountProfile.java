package com.fucar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "AccountProfile")
public class AccountProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profileID;

    @OneToOne
    @JoinColumn(name = "AccountID", nullable = false, unique = true)
    private Account account;

    private String fullName;
    private String email;
    private String mobile;
    private java.time.LocalDate birthday;

    // getters & setters
}
