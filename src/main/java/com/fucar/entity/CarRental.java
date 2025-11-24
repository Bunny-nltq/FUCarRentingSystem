package com.fucar.entity;

import jakarta.persistence.*;
import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "CarRental")
public class CarRental {

    // REAL FIELDS (Hibernate dùng)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RentalID")
    private Integer rentalId;

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
    private String status;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;


    // =============== JavaFX Properties (TableView dùng) ===============

    @Transient
    private IntegerProperty rentalIdProperty = new SimpleIntegerProperty();

    @Transient
    private ObjectProperty<LocalDate> pickupDateProperty = new SimpleObjectProperty<>();

    @Transient
    private ObjectProperty<LocalDate> returnDateProperty = new SimpleObjectProperty<>();

    @Transient
    private DoubleProperty rentPriceProperty = new SimpleDoubleProperty();

    @Transient
    private StringProperty statusProperty = new SimpleStringProperty();


    // =============== Constructors ===============

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public CarRental() {}

    public CarRental(Customer customer, Car car, LocalDate pickupDate,
                     LocalDate returnDate, Double rentPrice, String status) {

        this.customer = customer;
        this.car = car;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.rentPrice = rentPrice;
        this.status = status;

        syncProperties();
    }


    // =============== Sync ENTITY → PROPERTY (Bắt buộc!) ===============

    @PostLoad
    public void syncProperties() {
        if (rentalId != null) rentalIdProperty.set(rentalId);
        pickupDateProperty.set(pickupDate);
        returnDateProperty.set(returnDate);
        rentPriceProperty.set(rentPrice);
        statusProperty.set(status);
    }


    // =============== Getters & Setters ===============

    public Integer getRentalId() { return rentalId; }
    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
        this.rentalIdProperty.set(rentalId);
    }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
        this.pickupDateProperty.set(pickupDate);
    }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.returnDateProperty.set(returnDate);
    }

    public Double getRentPrice() { return rentPrice; }
    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
        this.rentPriceProperty.set(rentPrice);
    }

    public String getStatus() { return status; }
    public void setStatus(String status) {
        this.status = status;
        this.statusProperty.set(status);
    }

    public LocalDateTime getCreatedAt() { return createdAt; }


    // =============== JavaFX Property getters để TableView map ===============

    public IntegerProperty rentalIdProperty() { return rentalIdProperty; }

    public ObjectProperty<LocalDate> startDateProperty() { return pickupDateProperty; }

    public ObjectProperty<LocalDate> endDateProperty() { return returnDateProperty; }

    public DoubleProperty rentPriceProperty() { return rentPriceProperty; }

    public StringProperty statusProperty() { return statusProperty; }


    // =============== Helper ===============

    public Integer getCustomerID() {
        return (customer != null) ? customer.getCustomerID() : null;
    }

    public Integer getCarID() {
        return (car != null) ? car.getCarID() : null;
    }


    @Override
    public String toString() {
        return "CarRental{" +
                "rentalId=" + rentalId +
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
