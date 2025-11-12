package com.fucar.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carID;

    @Column(nullable = false)
    private String carName;

    private Integer carModelYear;
    private String color;
    private Integer capacity;

    @Column(length = 500)
    private String description;

    private LocalDate importDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProducerID", nullable = false)
    private CarProducer producer;

    @Column(nullable = false)
    private Double rentPrice;

    @Column(nullable = false)
    private String status; // AVAILABLE / RENTED / MAINTENANCE

    @Column(unique = true)
    private String licensePlate;

    // getters & setters
}
