package com.fucar.service;

import java.util.List;

import com.fucar.entity.Car;
import com.fucar.repository.CarRentalRepository;
import com.fucar.repository.CarRepository;

public class CarService {

    private final CarRepository repo = new CarRepository();
    private final CarRentalRepository rentalRepo = new CarRentalRepository();

    public List<Car> getAllCars() {
        return repo.findAll();
    }

    public Car getById(int id) {
        return repo.findById(id);
    }

    public void save(Car car) {
        repo.save(car);
    }

    public void update(Car car) {
        repo.update(car);
    }

    // Xóa xe chỉ nếu chưa có trong giao dịch thuê
    public boolean canDeleteCar(Integer carId) {
        return !rentalRepo.existsByCarId(carId);
    }

    // Xóa xe an toàn
    public void deleteCar(Integer carId) {
        if (canDeleteCar(carId)) {
            repo.delete(carId);
        } else {
            throw new IllegalStateException("Cannot delete car. Car is involved in rental transactions.");
        }
    }

    // Cập nhật trạng thái xe khi có giao dịch
    public void updateCarStatus(Integer carId, String status) {
        Car car = repo.findById(carId);
        if (car != null) {
            car.setStatus(status);
            repo.update(car);
        }
    }
}
