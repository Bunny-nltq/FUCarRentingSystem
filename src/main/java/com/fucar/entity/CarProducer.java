package com.fucar.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "CarProducer")
public class CarProducer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer producerID;

    @Column(nullable = false, unique = true)
    private String producerName;

    private String address;
    private String country;

    // mappedBy trong Car sẽ là 'producer'
    @OneToMany(mappedBy = "producer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Car> cars;

    // constructors, getters, setters

    public CarProducer() {}

    public Integer getProducerID() {
        return producerID;
    }

    public void setProducerID(Integer producerID) {
        this.producerID = producerID;
    }

    public String getProducerName() {
        return producerName;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
}
