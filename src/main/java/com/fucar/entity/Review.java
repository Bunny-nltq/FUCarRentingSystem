package com.fucar.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    private Integer reviewID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CarID", nullable = false)
    private Car car;

    @Column(name = "ReviewStar", nullable = false)
    private Integer reviewStar; // 1..5

    @Column(name = "Comment", length = 1000)
    private String comment;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    // ============================
    // Constructors
    // ============================

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(Customer customer, Car car, Integer reviewStar, String comment) {
        this.customer = customer;
        this.car = car;
        this.reviewStar = reviewStar;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // ============================
    // Getters & Setters
    // ============================

    public Integer getReviewID() {
        return reviewID;
    }

    public void setReviewID(Integer reviewID) {
        this.reviewID = reviewID;
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

    public Integer getReviewStar() {
        return reviewStar;
    }

    public void setReviewStar(Integer reviewStar) {
        this.reviewStar = reviewStar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ============================
    // Helper methods for controller/service
    // ============================

    public Integer getCustomerID() {
        return customer != null ? customer.getCustomerID() : null;
    }

    public Integer getCarID() {
        return car != null ? car.getCarID() : null;
    }

    // ============================
    // Safe toString (tránh lỗi lazy)
    // ============================

    @Override
    public String toString() {
        return "Review{" +
                "reviewID=" + reviewID +
                ", customerID=" + getCustomerID() +
                ", carID=" + getCarID() +
                ", reviewStar=" + reviewStar +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
