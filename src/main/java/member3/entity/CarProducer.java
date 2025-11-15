package member3.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity: CarProducer (Hãng sản xuất xe)
 * Đại diện cho một hãng sản xuất xe
 */
@Entity
@Table(name = "car_producer")
public class CarProducer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producer_id")
    private int producerId;
    
    @Column(name = "producer_name", nullable = false, unique = true)
    private String producerName;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "founded_year")
    private int foundedYear;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    
    // 1:N relationship with Car (One producer has many cars)
    @OneToMany(mappedBy = "producer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Car> cars = new HashSet<>();
    
    // ===== CONSTRUCTORS =====
    
    public CarProducer() {}
    
    public CarProducer(String producerName, String country) {
        this.producerName = producerName;
        this.country = country;
    }
    
    public CarProducer(String producerName, String country, int foundedYear) {
        this.producerName = producerName;
        this.country = country;
        this.foundedYear = foundedYear;
    }
    
    // ===== GETTERS & SETTERS =====
    
    public int getProducerId() {
        return producerId;
    }
    
    public void setProducerId(int producerId) {
        this.producerId = producerId;
    }
    
    public String getProducerName() {
        return producerName;
    }
    
    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public int getFoundedYear() {
        return foundedYear;
    }
    
    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public Set<Car> getCars() {
        return cars;
    }
    
    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
    
    // ===== VALIDATION =====
    
    public boolean isValid() {
        if (producerName == null || producerName.trim().isEmpty()) return false;
        if (foundedYear > 0 && (foundedYear < 1800 || foundedYear > java.time.LocalDate.now().getYear())) {
            return false;
        }
        return true;
    }
    
    // ===== TO STRING =====
    
    @Override
    public String toString() {
        return String.format("CarProducer{id=%d, name='%s', country='%s', founded=%d, cars=%d}",
                producerId, producerName, country, foundedYear, cars.size());
    }
}
