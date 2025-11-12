package com.fucar.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerID;

    @Column(nullable = false)
    private String customerName;

    private String mobile;
    private LocalDate birthday;
    private String identityCard;
    private String licenceNumber;
    private LocalDate licenceDate;
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountID")
    private Account account;

    // getters & setters
}
