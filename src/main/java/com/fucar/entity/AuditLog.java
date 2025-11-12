package com.fucar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AuditLog")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AccountID")
    private Account account;

    private String action;
    private String entityName;
    private String entityId;

    @Column(length = 1000)
    private String detail;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters & setters
}