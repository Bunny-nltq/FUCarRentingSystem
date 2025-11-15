package member3;

import member3.entity.Car;
import member3.entity.CarProducer;
import member3.service.CarService;
import member3.service.CarProducerService;
import member3.util.HibernateUtil;

import java.util.List;

/**
 * Test: TestCarManagement
 * Thử nghiệm Service Layer:
 * - Validation dữ liệu (năm sản xuất, giá, biển số)
 * - Kiểm tra điều kiện xóa xe (không xóa xe RENTING/MAINTENANCE)
 * - CRUD operations qua Service
 */
public class TestCarManagement {
    
    public static void main(String[] args) {
        CarService carService = new CarService();
        CarProducerService producerService = new CarProducerService();
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🚗 TEST CAR MANAGEMENT SYSTEM - SERVICE LAYER 🚗");
        System.out.println("=".repeat(70));
        
        // 1️⃣ THÊM HÃN SẢN XUẤT
        System.out.println("\n1️⃣ THÊM HÃN SẢN XUẤT:");
        System.out.println("-".repeat(70));
        
        CarProducer toyota = new CarProducer("Toyota", "Japan", 1937);
        CarProducer honda = new CarProducer("Honda", "Japan", 1946);
        CarProducer ford = new CarProducer("Ford", "USA", 1903);
        
        producerService.addProducer(toyota);
        producerService.addProducer(honda);
        producerService.addProducer(ford);
        
        // 2️⃣ THÊM XE VỚI VALIDATION
        System.out.println("\n2️⃣ THÊM XE VỚI VALIDATION:");
        System.out.println("-".repeat(70));
        
        Car car1 = new Car("Toyota Vios", "Vios", 2021, "29A-12345", 300000);
        car1.setProducer(toyota);
        carService.addCar(car1);
        
        Car car2 = new Car("Honda Civic", "Civic", 2020, "29A-54321", 400000);
        car2.setProducer(honda);
        carService.addCar(car2);
        
        Car car3 = new Car("Toyota Camry", "Camry", 2022, "29A-11111", 500000);
        car3.setProducer(toyota);
        carService.addCar(car3);
        
        Car car4 = new Car("Ford Ranger", "Ranger", 2023, "29A-99999", 600000);
        car4.setProducer(ford);
        carService.addCar(car4);
        
        // 3️⃣ TEST VALIDATION - NĂNG SẢN XUẤT SAI
        System.out.println("\n3️⃣ TEST VALIDATION - NĂM SẢN XUẤT SAI:");
        System.out.println("-".repeat(70));
        
        Car invalidYearCar = new Car("Test Car", "Test", 1800, "99X-00000", 250000); // Năm < 1900
        invalidYearCar.setProducer(toyota);
        System.out.println("Thêm xe với năm sản xuất 1800 (phải lỗi):");
        carService.addCar(invalidYearCar);
        
        // 4️⃣ TEST VALIDATION - GIÁ SAI
        System.out.println("\n4️⃣ TEST VALIDATION - GIÁ SAI:");
        System.out.println("-".repeat(70));
        
        Car invalidPriceCar = new Car("Test Car 2", "Test2", 2020, "99Y-00000", -50000); // Giá âm
        invalidPriceCar.setProducer(honda);
        System.out.println("Thêm xe với giá âm -50000 (phải lỗi):");
        carService.addCar(invalidPriceCar);
        
        // 5️⃣ TEST VALIDATION - BIỂN SỐ TRÙNG
        System.out.println("\n5️⃣ TEST VALIDATION - BIỂN SỐ TRÙNG:");
        System.out.println("-".repeat(70));
        
        Car duplicatePlateCar = new Car("Another Toyota", "Altis", 2021, "29A-12345", 350000); // Biển số trùng
        duplicatePlateCar.setProducer(toyota);
        System.out.println("Thêm xe với biển số trùng '29A-12345' (phải lỗi):");
        carService.addCar(duplicatePlateCar);
        
        // 6️⃣ DANH SÁCH TẤT CẢ XE
        System.out.println("\n6️⃣ DANH SÁCH TẤT CẢ XE:");
        System.out.println("-".repeat(70));
        
        List<Car> allCars = carService.getAllCars();
        allCars.forEach(car -> System.out.println("  " + car));
        
        // 7️⃣ CẬP NHẬT GIÁ XE
        System.out.println("\n7️⃣ CẬP NHẬT GIÁ XE:");
        System.out.println("-".repeat(70));
        
        System.out.println("Cập nhật giá xe ID=1 thành 320000:");
        carService.updateRentalPrice(1, 320000);
        Car updatedCar = carService.getCarById(1);
        System.out.println("Giá mới: " + updatedCar.getRentalPrice());
        
        // 8️⃣ THAY ĐỔI TRẠNG THÁI XE
        System.out.println("\n8️⃣ THAY ĐỔI TRẠNG THÁI XE:");
        System.out.println("-".repeat(70));
        
        System.out.println("Đổi xe ID=1 thành RENTING:");
        carService.changeCarStatus(1, Car.CarStatus.RENTING);
        
        System.out.println("Đổi xe ID=2 thành MAINTENANCE:");
        carService.changeCarStatus(2, Car.CarStatus.MAINTENANCE);
        
        System.out.println("Xe ID=3, 4 vẫn ở AVAILABLE");
        
        // 9️⃣ ⚠️ TEST XÓA XE - KIỂM TRA ĐIỀU KIỆN GIAO DỊCH
        System.out.println("\n9️⃣ ⚠️ TEST XÓA XE - KIỂM TRA ĐIỀU KIỆN:");
        System.out.println("-".repeat(70));
        
        System.out.println("🚫 Thử xóa xe ID=1 (đang RENTING - phải thất bại):");
        carService.deleteCar(1);
        
        System.out.println("\n🚫 Thử xóa xe ID=2 (đang MAINTENANCE - phải thất bại):");
        carService.deleteCar(2);
        
        System.out.println("\n✅ Thử xóa xe ID=3 (AVAILABLE - phải thành công):");
        carService.deleteCar(3);
        
        System.out.println("\n✅ Thử xóa xe ID=4 (AVAILABLE - phải thành công):");
        carService.deleteCar(4);
        
        // 🔟 LẤY XE CÓ SẴN
        System.out.println("\n🔟 LẤY XE CÓ SẴN CHO THUÊ:");
        System.out.println("-".repeat(70));
        
        List<Car> availableCars = carService.getAvailableCars();
        if (availableCars.isEmpty()) {
            System.out.println("  Không có xe nào có sẵn");
        } else {
            availableCars.forEach(car -> System.out.println("  " + car));
        }
        
        // 1️⃣1️⃣ THỐNG KÊ HỆ THỐNG
        System.out.println("\n1️⃣1️⃣ THỐNG KÊ HỆ THỐNG:");
        System.out.println("-".repeat(70));
        carService.printStatistics();
        
        // 1️⃣2️⃣ TÌM XE THEO TÊN
        System.out.println("\n1️⃣2️⃣ TÌM XE THEO TÊN (Toyota):");
        System.out.println("-".repeat(70));
        
        List<Car> toyotaCars = carService.searchCarByName("Toyota");
        if (toyotaCars.isEmpty()) {
            System.out.println("  Không tìm thấy");
        } else {
            toyotaCars.forEach(car -> System.out.println("  " + car));
        }
        
        // 1️⃣3️⃣ TEST XÓA HÃN - KIỂM TRA CÓ XE LIÊN QUAN
        System.out.println("\n1️⃣3️⃣ TEST XÓA HÃN - KIỂM TRA CÓ XE LIÊN QUAN:");
        System.out.println("-".repeat(70));
        
        System.out.println("🚫 Thử xóa Toyota (còn xe liên quan - phải thất bại):");
        producerService.deleteProducer(1);
        
        System.out.println("\n✅ Thử xóa Ford (không có xe liên quan - phải thành công):");
        producerService.deleteProducer(3);
        
        // 1️⃣4️⃣ DANH SÁCH CUỐI CÙNG
        System.out.println("\n1️⃣4️⃣ DANH SÁCH XE CUỐI CÙNG:");
        System.out.println("-".repeat(70));
        
        allCars = carService.getAllCars();
        allCars.forEach(car -> System.out.println("  " + car));
        
        System.out.println("\n" + "=".repeat(70));
        System.out.println("✅ TEST HOÀN TẤT!");
        System.out.println("=".repeat(70));
        
        HibernateUtil.shutdown();
    }
}
