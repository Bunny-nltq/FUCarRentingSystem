package com.fucar.service;

import com.fucar.entity.Car;
import com.fucar.entity.CarRental;
import com.fucar.repository.CarRentalRepository;
import com.fucar.repository.CarRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CarRentalService {

    private final CarRentalRepository rentalRepo = new CarRentalRepository();
    private final CarRepository carRepo = new CarRepository();
    
    
    // ===================== LẤY DANH SÁCH THUÊ XE THEO KHÁCH HÀNG =====================
    public List<CarRental> getRentalsByCustomer(Integer customerId) {
        if (customerId == null) return List.of();
        // Giả định rằng Repository có phương thức này
        return rentalRepo.findByCustomerId(customerId); 
    }

    // ===================== TẠO ĐƠN THUÊ XE =====================
    public void createRental(CarRental rental) throws IllegalArgumentException {
        if (rental == null)
            throw new IllegalArgumentException("Rental cannot be null.");

        LocalDate pickup = rental.getPickupDate();
        LocalDate returned = rental.getReturnDate();
        if (pickup == null || returned == null)
            throw new IllegalArgumentException("Pickup and Return dates cannot be null.");
        if (!pickup.isBefore(returned))
            throw new IllegalArgumentException("Pickup date must be before Return date.");

        Car car = rental.getCar();
        if (car == null)
            throw new IllegalArgumentException("Rental must have a car assigned.");

        // Tự động tính giá nếu người dùng không nhập
        if (rental.getRentPrice() == null || rental.getRentPrice() == 0) {
            BigDecimal price = calculatePrice(car, pickup, returned);
            rental.setRentPrice(price.doubleValue());
        }

        // Nếu trạng thái đơn thuê là RENTED → cập nhật trạng thái xe
        if ("RENTED".equalsIgnoreCase(rental.getStatus())) {
            car.setStatus("RENTED");
            carRepo.update(car);
        }

        rentalRepo.save(rental);
    }

    // ===================== CẬP NHẬT ĐƠN THUÊ XE =====================
    public void updateRental(CarRental rental) throws IllegalArgumentException {
        // Không kiểm tra trạng thái xe khi update vì xe đang được thuê
        if (rental == null)
            throw new IllegalArgumentException("Rental cannot be null.");

        LocalDate pickup = rental.getPickupDate();
        LocalDate returned = rental.getReturnDate();
        if (pickup == null || returned == null)
            throw new IllegalArgumentException("Pickup and Return dates cannot be null.");
        if (!pickup.isBefore(returned))
            throw new IllegalArgumentException("Pickup date must be before Return date.");

        Car car = rental.getCar();
        if (car == null)
            throw new IllegalArgumentException("Rental must have a car assigned.");

        // Tính lại giá khi cập nhật đơn
        BigDecimal price = calculatePrice(rental.getCar(), rental.getPickupDate(), rental.getReturnDate());
        rental.setRentPrice(price.doubleValue());

        rentalRepo.update(rental);
    }

    // ===================== XÓA ĐƠN THUÊ XE =====================
    public void deleteRental(int rentalId) {
        CarRental rental = rentalRepo.findById(rentalId);
        if (rental != null) {
            Car car = rental.getCar();
            rentalRepo.delete(rental);

            // Sau khi xóa đơn → trả xe về trạng thái AVAILABLE
            car.setStatus("AVAILABLE");
            carRepo.update(car);
        }
    }

    // ===================== KIỂM TRA HỢP LỆ ĐƠN THUÊ XE =====================
    private void validateRental(CarRental rental) throws IllegalArgumentException {
        if (rental == null)
            throw new IllegalArgumentException("Rental cannot be null.");

        LocalDate pickup = rental.getPickupDate();
        LocalDate returned = rental.getReturnDate();
        if (pickup == null || returned == null)
            throw new IllegalArgumentException("Pickup and Return dates cannot be null.");
        if (!pickup.isBefore(returned))
            throw new IllegalArgumentException("Pickup date must be before Return date.");

        Car car = rental.getCar();
        if (car == null)
            throw new IllegalArgumentException("Rental must have a car assigned.");
        if (!"AVAILABLE".equalsIgnoreCase(car.getStatus()))
            throw new IllegalArgumentException("Car is not available for rental.");
    }

    // ===================== TÍNH GIÁ THUÊ XE =====================
    public BigDecimal calculatePrice(Car car, LocalDate pickup, LocalDate returned) {
        Double dailyPriceDouble = car.getRentPrice() != null ? car.getRentPrice() : 0.0;
        BigDecimal dailyPrice = BigDecimal.valueOf(dailyPriceDouble);
        
        long days = ChronoUnit.DAYS.between(pickup, returned);
        if (days <= 0) days = 1; // Tối thiểu 1 ngày

        BigDecimal dayCount = BigDecimal.valueOf(days);
        return dailyPrice.multiply(dayCount);
    }

    // ===================== LẤY TẤT CẢ ĐƠN THUÊ =====================
    public List<CarRental> getAll() {
        return rentalRepo.findAll();
    }

    public CarRental findById(int id) {
        return rentalRepo.findById(id);
    }

    // ===================== LỌC & SẮP XẾP =====================
    public List<CarRental> filterByDate(LocalDate start, LocalDate end) {
        return rentalRepo.filterByDate(start, end);
    }

    public List<CarRental> sortByPriceDesc() {
        return rentalRepo.sortByPriceDesc();
    }

    public List<CarRental> sortByPickupDateDesc() {
        return rentalRepo.sortByPickupDateDesc();
    }

    // ===================== LẤY ĐƠN THUÊ THEO ACCOUNT =====================
    public List<CarRental> getRentalsByAccount(Integer accountID) {
        if (accountID == null) return List.of();
        return rentalRepo.findByCustomerAccountId(accountID);
    }

    public boolean existsByCustomerId(Integer customerID) {
        // TODO: Chưa triển khai
        return false;
    }
}
