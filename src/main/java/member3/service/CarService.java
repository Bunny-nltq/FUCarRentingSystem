package member3.service;

import member3.entity.Car;
import member3.entity.CarProducer;
import member3.repository.CarRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Service: CarService
 * Xử lý logic nghiệp vụ cho Car
 * - Validate dữ liệu
 * - Kiểm tra điều kiện trước khi xóa (không xóa xe đang cho thuê/bảo dưỡng)
 * - Quản lý trạng thái xe
 */
public class CarService {
    
    private final CarRepository carRepository = new CarRepository();
    
    // ===== VALIDATION METHODS =====
    
    /**
     * Validate năm sản xuất (1900 - năm hiện tại)
     */
    private boolean isValidYear(int year) {
        int currentYear = LocalDate.now().getYear();
        if (year < 1900 || year > currentYear) {
            System.err.println("❌ Năm sản xuất phải từ 1900 đến " + currentYear);
            return false;
        }
        return true;
    }
    
    /**
     * Validate giá thuê > 0
     */
    private boolean isValidPrice(double price) {
        if (price <= 0) {
            System.err.println("❌ Giá thuê phải lớn hơn 0");
            return false;
        }
        return true;
    }
    
    /**
     * Validate biển số (không được trống)
     */
    private boolean isValidLicensePlate(String plate) {
        if (plate == null || plate.trim().isEmpty()) {
            System.err.println("❌ Biển số không được trống");
            return false;
        }
        return true;
    }
    
    /**
     * Validate tên xe (không được trống)
     */
    private boolean isValidCarName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.err.println("❌ Tên xe không được trống");
            return false;
        }
        return true;
    }
    
    /**
     * Kiểm tra biển số không bị trùng
     */
    private boolean isUniqueLicensePlate(String plate, int excludeCarId) {
        Car existing = carRepository.findByLicensePlate(plate);
        if (existing != null && existing.getCarId() != excludeCarId) {
            System.err.println("❌ Biển số '" + plate + "' đã tồn tại!");
            return false;
        }
        return true;
    }
    
    /**
     * Validate toàn bộ dữ liệu xe
     */
    private boolean validateCar(Car car) {
        if (!isValidCarName(car.getCarName())) return false;
        if (!isValidLicensePlate(car.getLicensePlate())) return false;
        if (!isValidPrice(car.getRentalPrice())) return false;
        if (car.getYearOfProduction() > 0 && !isValidYear(car.getYearOfProduction())) {
            return false;
        }
        return true;
    }
    
    // ===== CREATE =====
    
    /**
     * Thêm xe mới với kiểm tra validation
     */
    public boolean addCar(Car car) {
        if (car == null) {
            System.err.println("❌ Car không được null");
            return false;
        }
        
        // Kiểm tra validation
        if (!validateCar(car)) {
            return false;
        }
        
        // Kiểm tra biển số duy nhất
        if (!isUniqueLicensePlate(car.getLicensePlate(), -1)) {
            return false;
        }
        
        // Lưu vào database
        return carRepository.save(car);
    }
    
    // ===== READ =====
    
    /**
     * Lấy xe theo ID
     */
    public Car getCarById(int carId) {
        return carRepository.findById(carId);
    }
    
    /**
     * Lấy tất cả xe
     */
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    
    /**
     * Tìm xe theo tên
     */
    public List<Car> searchCarByName(String name) {
        return carRepository.findByName(name);
    }
    
    /**
     * Lấy xe có sẵn cho thuê
     */
    public List<Car> getAvailableCars() {
        return carRepository.getAvailableCars();
    }
    
    /**
     * Lấy xe theo trạng thái
     */
    public List<Car> getCarsByStatus(Car.CarStatus status) {
        return carRepository.findByStatus(status);
    }
    
    /**
     * Tìm xe theo hãng sản xuất
     */
    public List<Car> getCarsByProducer(CarProducer producer) {
        return carRepository.findByProducer(producer);
    }
    
    /**
     * Tìm xe theo khoảng giá
     */
    public List<Car> getCarsByPriceRange(double minPrice, double maxPrice) {
        return carRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    // ===== UPDATE =====
    
    /**
     * Cập nhật thông tin xe
     */
    public boolean updateCar(Car car) {
        if (car == null || car.getCarId() <= 0) {
            System.err.println("❌ Car ID không hợp lệ");
            return false;
        }
        
        // Validate dữ liệu
        if (!validateCar(car)) {
            return false;
        }
        
        // Kiểm tra biển số không bị trùng với car khác
        if (!isUniqueLicensePlate(car.getLicensePlate(), car.getCarId())) {
            return false;
        }
        
        return carRepository.update(car);
    }
    
    /**
     * Thay đổi trạng thái xe
     */
    public boolean changeCarStatus(int carId, Car.CarStatus newStatus) {
        Car car = carRepository.findById(carId);
        if (car == null) {
            System.err.println("❌ Xe không tồn tại");
            return false;
        }
        
        car.setCarStatus(newStatus);
        return carRepository.update(car);
    }
    
    /**
     * Cập nhật giá thuê xe
     */
    public boolean updateRentalPrice(int carId, double newPrice) {
        if (!isValidPrice(newPrice)) {
            return false;
        }
        
        Car car = carRepository.findById(carId);
        if (car == null) {
            System.err.println("❌ Xe không tồn tại");
            return false;
        }
        
        car.setRentalPrice(newPrice);
        return carRepository.update(car);
    }
    
    // ===== DELETE (WITH BUSINESS RULES) =====
    
    /**
     * ⚠️ XÓA XE - KIỂM TRA ĐIỀU KIỆN GIAO DỊCH
     * 
     * Quy tắc:
     * - KHÔNG xóa xe đang RENTING (Đang cho thuê)
     * - KHÔNG xóa xe đang MAINTENANCE (Đang bảo dưỡng)
     * - CHỈ xóa xe ở trạng thái AVAILABLE (Có sẵn)
     */
    public boolean deleteCar(int carId) {
        Car car = carRepository.findById(carId);
        
        if (car == null) {
            System.err.println("❌ Xe không tồn tại");
            return false;
        }
        
        // Kiểm tra điều kiện xóa
        if (car.getCarStatus() == Car.CarStatus.RENTING) {
            System.err.println("❌ KHÔNG THỂ XÓA! Xe đang được cho thuê (RENTING)");
            System.err.println("   Biển số: " + car.getLicensePlate());
            System.err.println("   Hãng: " + car.getProducer().getProducerName());
            return false;
        }
        
        if (car.getCarStatus() == Car.CarStatus.MAINTENANCE) {
            System.err.println("❌ KHÔNG THỂ XÓA! Xe đang bảo dưỡng (MAINTENANCE)");
            System.err.println("   Biển số: " + car.getLicensePlate());
            System.err.println("   Hãng: " + car.getProducer().getProducerName());
            return false;
        }
        
        // Chỉ xóa được khi xe ở trạng thái AVAILABLE
        if (car.getCarStatus() != Car.CarStatus.AVAILABLE) {
            System.err.println("❌ KHÔNG THỂ XÓA! Xe không ở trạng thái có sẵn");
            return false;
        }
        
        return carRepository.delete(carId);
    }
    
    // ===== STATISTICS =====
    
    /**
     * Đếm tổng số xe
     */
    public long getTotalCars() {
        return carRepository.count();
    }
    
    /**
     * Đếm xe có sẵn
     */
    public long countAvailableCars() {
        return carRepository.countByStatus(Car.CarStatus.AVAILABLE);
    }
    
    /**
     * Đếm xe đang cho thuê
     */
    public long countRentingCars() {
        return carRepository.countByStatus(Car.CarStatus.RENTING);
    }
    
    /**
     * Đếm xe đang bảo dưỡng
     */
    public long countMaintenanceCars() {
        return carRepository.countByStatus(Car.CarStatus.MAINTENANCE);
    }
    
    /**
     * In thống kê hệ thống
     */
    public void printStatistics() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 THỐNG KÊ HỆ THỐNG XE");
        System.out.println("=".repeat(60));
        System.out.println("  Tổng số xe: " + getTotalCars());
        System.out.println("  ✅ Xe có sẵn: " + countAvailableCars());
        System.out.println("  🚗 Xe đang cho thuê: " + countRentingCars());
        System.out.println("  🔧 Xe đang bảo dưỡng: " + countMaintenanceCars());
        System.out.println("=".repeat(60));
    }
}
