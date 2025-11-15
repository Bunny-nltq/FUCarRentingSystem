package com.fucar.service;

import com.fucar.entity.Car;
import com.fucar.entity.CarRental;
import com.fucar.repository.CarRentalRepository;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

public class CarRentalService {

    private final CarRentalRepository repo = new CarRentalRepository();

    // Validate + tính giá thuê tự động
    public boolean createRental(CarRental rental) {
        if (!validateDates(rental.getPickupDate(), rental.getReturnDate())) {
            return false;
        }

        double price = calculatePrice(rental.getCar(), rental.getPickupDate(), rental.getReturnDate());
        rental.setRentPrice(price);

        repo.save(rental);
        return true;
    }

    public boolean updateRental(CarRental rental) {
        if (!validateDates(rental.getPickupDate(), rental.getReturnDate())) {
            return false;
        }

        rental.setRentPrice(calculatePrice(
                rental.getCar(),
                rental.getPickupDate(),
                rental.getReturnDate()
        ));

        repo.update(rental);
        return true;
    }

    // Validate điều kiện ngày
    public boolean validateDates(LocalDate pickup, LocalDate returned) {
        return pickup != null && returned != null && pickup.isBefore(returned);
    }

    // Tính giá thuê
    public double calculatePrice(Car car, LocalDate pickup, LocalDate returned) {
        long days = ChronoUnit.DAYS.between(pickup, returned);
        if (days <= 0) days = 1;
        return days * car.getPricePerDay();
    }

    // Repository wrappers
    public List<CarRental> getAll() { return repo.findAll(); }
    public CarRental findById(int id) { return repo.findById(id); }
    public void delete(int id) { repo.delete(id); }
    public List<CarRental> filterByDate(LocalDate s, LocalDate e) { return repo.filterByDate(s, e); }
    public List<CarRental> sortByPrice() { return repo.sortByPriceDesc(); }
    public List<CarRental> sortByDate() { return repo.sortByPickupDateDesc(); }
}
