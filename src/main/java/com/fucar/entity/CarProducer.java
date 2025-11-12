package com.fucar.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "CarProducer")
public class CarProducer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer producerID;

    @Column(nullable = false)
    private String producerName;

    private String address;
    private String country;

    @OneToMany(mappedBy = "producer", cascade = CascadeType.ALL)
    private List<Car> cars;

    // getters & setters
}
