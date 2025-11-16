package com.fucar.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CarRental")
public class CarRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RentalID")
    private Integer rentalID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CarID", nullable = false)
    private Car car;

    @Column(name = "PickupDate", nullable = false)
    private LocalDate pickupDate;

    @Column(name = "ReturnDate", nullable = false)
    private LocalDate returnDate;

    @Column(name = "RentPrice", nullable = false)
    private Double rentPrice;

    @Column(name = "Status", nullable = false, length = 20)
    private String status; // CART / PLACED / PAID / IN_USE / RETURNED / CLOSED

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // ============================
    // Constructors
    // ============================

    public CarRental() {
        this.createdAt = LocalDateTime.now();
    }

    public CarRental(Customer customer, Car car, LocalDate pickupDate,
                     LocalDate returnDate, Double rentPrice, String status) {
        this.customer = customer;
        this.car = car;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.rentPrice = rentPrice;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // ============================
    // Getters & Setters
    // ============================

    public Integer getRentalID() {
        return rentalID;
    }

    public void setRentalID(Integer rentalID) {
        this.rentalID = rentalID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public Double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ============================
    // Additional helper methods
    // ============================

    // Các method này giúp controller/service gọi trực tiếp mà không lỗi
    public Integer getCustomerID() {
        return customer != null ? customer.getCustomerID() : null;
    }

    public Integer getCarID() {
        return car != null ? car.getCarID() : null;
    }

    // ============================
    // Safe toString (không đụng lazy)
    // ============================

    @Override
    public String toString() {
        return "CarRental{" +
                "rentalID=" + rentalID +
                ", customerID=" + getCustomerID() +
                ", carID=" + getCarID() +
                ", pickupDate=" + pickupDate +
                ", returnDate=" + returnDate +
                ", rentPrice=" + rentPrice +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
