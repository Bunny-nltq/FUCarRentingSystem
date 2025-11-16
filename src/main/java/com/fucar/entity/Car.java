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

    // ===========================
    // Constructors
    // ===========================
    public Car() {}

    public Car(String carName, Integer carModelYear, String color, Integer capacity,
               String description, LocalDate importDate, CarProducer producer,
               Double rentPrice, String status, String licensePlate) {
        this.carName = carName;
        this.carModelYear = carModelYear;
        this.color = color;
        this.capacity = capacity;
        this.description = description;
        this.importDate = importDate;
        this.producer = producer;
        this.rentPrice = rentPrice;
        this.status = status;
        this.licensePlate = licensePlate;
    }

    // ===========================
    // Getters
    // ===========================
    public Integer getCarID() { return carID; }
    public String getCarName() { return carName; }
    public Integer getCarModelYear() { return carModelYear; }
    public String getColor() { return color; }
    public Integer getCapacity() { return capacity; }
    public String getDescription() { return description; }
    public LocalDate getImportDate() { return importDate; }
    public CarProducer getProducer() { return producer; }
    public Double getRentPrice() { return rentPrice; }
    public String getStatus() { return status; }
    public String getLicensePlate() { return licensePlate; }

    /**
     * Alias method để code cũ vẫn chạy
     */
    public Double getPricePerDay() {
        return rentPrice;
    }

    // ===========================
    // Setters
    // ===========================
    public void setCarID(Integer carID) { this.carID = carID; }
    public void setCarName(String carName) { this.carName = carName; }
    public void setCarModelYear(Integer carModelYear) { this.carModelYear = carModelYear; }
    public void setColor(String color) { this.color = color; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public void setDescription(String description) { this.description = description; }
    public void setImportDate(LocalDate importDate) { this.importDate = importDate; }
    public void setProducer(CarProducer producer) { this.producer = producer; }
    public void setRentPrice(Double rentPrice) { this.rentPrice = rentPrice; }
    public void setStatus(String status) { this.status = status; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    // ===========================
    // Safe toString
    // ===========================
    @Override
    public String toString() {
        return "Car{" +
                "carID=" + carID +
                ", carName='" + carName + '\'' +
                ", carModelYear=" + carModelYear +
                ", color='" + color + '\'' +
                ", capacity=" + capacity +
                ", description='" + description + '\'' +
                ", importDate=" + importDate +
                ", producerID=" + (producer != null ? producer.getProducerID() : null) +
                ", rentPrice=" + rentPrice +
                ", status='" + status + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }
}
