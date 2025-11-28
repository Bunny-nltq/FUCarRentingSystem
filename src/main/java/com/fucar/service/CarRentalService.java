package com.fucar.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.fucar.entity.Car;
import com.fucar.entity.CarRental;
import com.fucar.entity.Customer;
import com.fucar.repository.CarRentalRepository;
import com.fucar.repository.CarRepository;
import com.fucar.repository.CustomerRepository;

public class CarRentalService {

    private final CarRentalRepository repo = new CarRentalRepository();
    private final CustomerRepository customerRepo = new CustomerRepository();
    private final CarRepository carRepo = new CarRepository();

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

    // Create rental with customer ID and car ID
    public void createRental(CarRental rental, int customerId, int carId) {
        try {
            Customer customer = customerRepo.findById(customerId);
            Car car = carRepo.findById(carId);
            
            if (customer != null && car != null) {
                rental.setCustomer(customer);
                rental.setCar(car);
                
                // Tính giá
                double price = calculatePrice(car, rental.getPickupDate(), rental.getReturnDate());
                rental.setRentPrice(price);
                
                repo.save(rental);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating rental: " + e.getMessage());
        }
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

    public void update(CarRental rental) {
        repo.update(rental);
    }

    // Validate điều kiện ngày
    public boolean validateDates(LocalDate pickup, LocalDate returned) {
        return pickup != null && returned != null && pickup.isBefore(returned);
    }

    // Tính giá thuê
    public double calculatePrice(Car car, LocalDate pickup, LocalDate returned) {
        long days = ChronoUnit.DAYS.between(pickup, returned);
        if (days <= 0) days = 1;
        return days * car.getRentPrice();
    }
    
    public boolean existsByCustomerId(Integer customerId) {
        return repo.existsByCustomerId(customerId);
    }


    // Repository wrappers
    public List<CarRental> getAll() { return repo.findAll(); }
    public CarRental findById(int id) { return repo.findById(id); }
    public void delete(int id) { repo.delete(id); }
    public List<CarRental> filterByDate(LocalDate s, LocalDate e) { return repo.filterByDate(s, e); }
    public List<CarRental> sortByPrice() { return repo.sortByPriceDesc(); }
    public List<CarRental> sortByDate() { return repo.sortByPickupDateDesc(); }
}
