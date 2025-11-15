package member3.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity: Car (Xe)
 * Đại diện cho một chiếc xe trong hệ thống cho thuê
 */
@Entity
@Table(name = "car")
public class Car {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private int carId;
    
    @Column(name = "car_name", nullable = false)
    private String carName;
    
    @Column(name = "model")
    private String model;
    
    @Column(name = "year_of_production")
    private int yearOfProduction;
    
    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;
    
    @Column(name = "rental_price")
    private double rentalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "car_status")
    private CarStatus carStatus = CarStatus.AVAILABLE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id")
    private CarProducer producer;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate = LocalDateTime.now();
    
    // ===== CONSTRUCTORS =====
    
    public Car() {}
    
    public Car(String carName, String licensePlate, double rentalPrice) {
        this.carName = carName;
        this.licensePlate = licensePlate;
        this.rentalPrice = rentalPrice;
    }
    
    public Car(String carName, String model, int yearOfProduction, 
               String licensePlate, double rentalPrice) {
        this.carName = carName;
        this.model = model;
        this.yearOfProduction = yearOfProduction;
        this.licensePlate = licensePlate;
        this.rentalPrice = rentalPrice;
    }
    
    // ===== GETTERS & SETTERS =====
    
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public String getCarName() {
        return carName;
    }
    
    public void setCarName(String carName) {
        this.carName = carName;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public int getYearOfProduction() {
        return yearOfProduction;
    }
    
    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public double getRentalPrice() {
        return rentalPrice;
    }
    
    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }
    
    public CarStatus getCarStatus() {
        return carStatus;
    }
    
    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }
    
    public CarProducer getProducer() {
        return producer;
    }
    
    public void setProducer(CarProducer producer) {
        this.producer = producer;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    // ===== VALIDATION =====
    
    public boolean isValid() {
        if (carName == null || carName.trim().isEmpty()) return false;
        if (licensePlate == null || licensePlate.trim().isEmpty()) return false;
        if (rentalPrice <= 0) return false;
        if (yearOfProduction > 0 && (yearOfProduction < 1900 || yearOfProduction > java.time.LocalDate.now().getYear())) {
            return false;
        }
        return true;
    }
    
    // ===== TO STRING =====
    
    @Override
    public String toString() {
        return String.format("Car{id=%d, name='%s', model='%s', year=%d, plate='%s', price=%.0f, status=%s, producer=%s}",
                carId, carName, model, yearOfProduction, licensePlate, rentalPrice, carStatus,
                producer != null ? producer.getProducerName() : "null");
    }
    
    // ===== ENUM: CAR STATUS =====
    
    public enum CarStatus {
        AVAILABLE("Có sẵn"),
        RENTING("Đang cho thuê"),
        MAINTENANCE("Bảo dưỡng");
        
        public final String displayName;
        
        CarStatus(String displayName) {
            this.displayName = displayName;
        }
    }
}
