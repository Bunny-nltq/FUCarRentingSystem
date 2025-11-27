package com.fucar.service;

import com.fucar.entity.Car;
import com.fucar.repository.CarRepository;

import java.util.List;

public class CarService {

    private final CarRepository repo = new CarRepository();

    public List<Car> getAllCars() {
        return repo.findAll();
    }

    public Car getById(int id) {
        return repo.findById(id);
    }
}
