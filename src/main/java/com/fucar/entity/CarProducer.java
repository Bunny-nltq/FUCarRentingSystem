package com.fucar.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "CarProducer")
public class CarProducer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer producerID;

    @Column(nullable = false)
    private String name;

    public CarProducer() {}

    public CarProducer(String name) {
        this.name = name;
    }

    // =========================== Getters & Setters ===========================
    public Integer getProducerID() {
        return producerID;
    }

    public void setProducerID(Integer producerID) {
        this.producerID = producerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CarProducer{" +
                "producerID=" + producerID +
                ", name='" + name + '\'' +
                '}';
    }
}
