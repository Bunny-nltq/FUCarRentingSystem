package com.fucar.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carID;

    @Column(nullable = false)
    private String carName;
    
    private String status; // <-- Cần có trường status

    private Integer carModelYear;

    private String color;

    private Integer capacity;

    @Column(length = 500)
    private String description;

    private LocalDate importDate;

    // price per day
    @Column(nullable = false)
    private BigDecimal rentalPrice;

    // relationship to producer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producerID")
    private CarProducer producer;

    // constructors, getters, setters

    public Car() {}

    public Integer getCarID() {
        return carID;
    }

    public void setCarID(Integer carID) {
        this.carID = carID;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }
    
    public String getStatus() { return status; } // <-- Thêm cái này
    public void setStatus(String status) { this.status = status; }


    public Integer getCarModelYear() {
        return carModelYear;
    }

    public void setCarModelYear(Integer carModelYear) {
        this.carModelYear = carModelYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getImportDate() {
        return importDate;
    }

    public void setImportDate(LocalDate importDate) {
        this.importDate = importDate;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public CarProducer getProducer() {
        return producer;
    }

    public void setProducer(CarProducer producer) {
        this.producer = producer;
    }
}
