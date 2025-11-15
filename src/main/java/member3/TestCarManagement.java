package member3;

import java.util.List;

import member3.entity.Car;
import member3.entity.CarProducer;
import member3.repository.CarRepository;
import member3.util.HibernateUtil;

/**
 * Test: TestCarManagement
 * Thử nghiệm tất cả các hoạt động CRUD của Car và CarProducer
 */
public class TestCarManagement {
    
    public static void main(String[] args) {
        CarRepository carRepo = new CarRepository();
        
        System.out.println("=" .repeat(60));
        System.out.println("🚗 TEST CAR MANAGEMENT SYSTEM 🚗");
        System.out.println("=" .repeat(60));
        
        // 1️⃣ THÊM XE
        System.out.println("\n1️⃣ THÊM XE:");
        System.out.println("-" .repeat(60));
        
        CarProducer toyota = new CarProducer("Toyota", "Japan", 1937);
        CarProducer honda = new CarProducer("Honda", "Japan", 1946);
        
        Car car1 = new Car("Toyota Vios", "Vios", 2021, "29A-12345", 300000);
        car1.setProducer(toyota);
        car1.setCarStatus(Car.CarStatus.AVAILABLE);
        
        Car car2 = new Car("Honda Civic", "Civic", 2020, "29A-54321", 400000);
        car2.setProducer(honda);
        car2.setCarStatus(Car.CarStatus.AVAILABLE);
        
        Car car3 = new Car("Toyota Camry", "Camry", 2022, "29A-11111", 500000);
        car3.setProducer(toyota);
        car3.setCarStatus(Car.CarStatus.AVAILABLE);
        
        carRepo.save(car1);
        carRepo.save(car2);
        carRepo.save(car3);
        
        // 2️⃣ LẤY TẤT CẢ XE
        System.out.println("\n2️⃣ DANH SÁCH TẤT CẢ XE:");
        System.out.println("-" .repeat(60));
        
        List<Car> allCars = carRepo.findAll();
        for (Car car : allCars) {
            System.out.println("  " + car);
        }
        
        // 3️⃣ TÌM XE THEO BIỂN SỐ
        System.out.println("\n3️⃣ TÌM XE THEO BIỂN SỐ:");
        System.out.println("-" .repeat(60));
        
        Car foundCar = carRepo.findByLicensePlate("29A-12345");
        if (foundCar != null) {
            System.out.println("  Tìm thấy: " + foundCar);
        }
        
        // 4️⃣ CẬP NHẬT GIÁ XE
        System.out.println("\n4️⃣ CẬP NHẬT GIÁ XE:");
        System.out.println("-" .repeat(60));
        
        if (foundCar != null) {
            foundCar.setRentalPrice(320000);
            carRepo.update(foundCar);
            System.out.println("  Giá xe " + foundCar.getCarName() + " sau cập nhật: " + foundCar.getRentalPrice());
        }
        
        // 5️⃣ THAY ĐỔI TRẠNG THÁI XE
        System.out.println("\n5️⃣ THAY ĐỔI TRẠNG THÁI XE:");
        System.out.println("-" .repeat(60));
        
        carRepo.changeStatus(1, Car.CarStatus.RENTING);
        carRepo.changeStatus(2, Car.CarStatus.MAINTENANCE);
        
        // 6️⃣ LẤY XE CÓ SẴN
        System.out.println("\n6️⃣ XE CÓ SẴN CHO THUÊ:");
        System.out.println("-" .repeat(60));
        
        List<Car> availableCars = carRepo.getAvailableCars();
        for (Car car : availableCars) {
            System.out.println("  " + car);
        }
        
        // 7️⃣ TÌM XE THEO TÊN
        System.out.println("\n7️⃣ TÌM XE THEO TÊN (Toyota):");
        System.out.println("-" .repeat(60));
        
        List<Car> toyotaCars = carRepo.findByName("Toyota");
        for (Car car : toyotaCars) {
            System.out.println("  " + car);
        }
        
        // 8️⃣ THỐNG KÊ XE
        System.out.println("\n8️⃣ THỐNG KÊ XE:");
        System.out.println("-" .repeat(60));
        System.out.println("  Tổng số xe: " + carRepo.count());
        System.out.println("  Xe có sẵn: " + carRepo.countByStatus(Car.CarStatus.AVAILABLE));
        System.out.println("  Xe đang cho thuê: " + carRepo.countByStatus(Car.CarStatus.RENTING));
        System.out.println("  Xe bảo dưỡng: " + carRepo.countByStatus(Car.CarStatus.MAINTENANCE));
        
        // 9️⃣ XÓA XE
        System.out.println("\n9️⃣ XÓA XE:");
        System.out.println("-" .repeat(60));
        
        // Thử xóa xe đang RENTING (sẽ thất bại)
        System.out.println("  Thử xóa xe ID=1 (đang cho thuê):");
        carRepo.delete(1);
        
        // Xóa xe AVAILABLE (sẽ thành công)
        System.out.println("  Xóa xe ID=3 (có sẵn):");
        carRepo.delete(3);
        
        // 🔟 DANH SÁCH CUỐI CÙNG
        System.out.println("\n🔟 DANH SÁCH XE CUỐI CÙNG:");
        System.out.println("-" .repeat(60));
        
        allCars = carRepo.findAll();
        for (Car car : allCars) {
            System.out.println("  " + car);
        }
        
        System.out.println("\n" + "=" .repeat(60));
        System.out.println("✅ TEST HOÀN TẤT!");
        System.out.println("=" .repeat(60));
        
        // Đóng Hibernate
        HibernateUtil.shutdown();
    }
}
