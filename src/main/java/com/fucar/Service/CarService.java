package com.fucar.service;

import com.fucar.entity.Car;
import com.fucar.repository.CarRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CarService {

    private final CarRepository carRepo = new CarRepository();

    // Validate car before save/update
    public void validate(Car car) {
        if (car.getCarName() == null || car.getCarName().trim().isEmpty()) {
            throw new IllegalArgumentException("Car name is required");
        }

        Integer year = car.getCarModelYear();
        if (year == null) throw new IllegalArgumentException("Model year required");
        int currentYear = LocalDate.now().getYear();
        if (year < 1886 || year > currentYear) { // ô tô bắt đầu ~1886
            throw new IllegalArgumentException("Invalid model year");
        }

        BigDecimal price = car.getRentalPrice();
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Rental price must be > 0");
        }

        Integer capacity = car.getCapacity();
        if (capacity == null || capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be > 0");
        }
    }

    public Car createCar(Car car) {
        validate(car);
        return carRepo.save(car);
    }

    public Car updateCar(Car car) {
        validate(car);
        return carRepo.update(car);
    }

    public void deleteCar(Integer carId) {
        // kiểm tra giao dịch trước
        if (carRepo.hasRentals(carId)) {
            throw new IllegalStateException("Không thể xóa: xe đã có giao dịch thuê");
        }
        Optional<Car> opt = carRepo.findById(carId);
        opt.ifPresent(carRepo::delete);
    }

    public List<Car> getAllCars() {
        return carRepo.findAll();
    }
}
