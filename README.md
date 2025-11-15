# Member3 Car Management V2

Hệ thống quản lý cho thuê xe sử dụng **Hibernate ORM** và **MySQL**

## 📂 Cấu trúc Dự án

```
CarManagement/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── member3/
    │   │       ├── entity/
    │   │       │   ├── Car.java              (Entity: Xe)
    │   │       │   └── CarProducer.java      (Entity: Hãng sản xuất)
    │   │       ├── repository/
    │   │       │   └── CarRepository.java    (CRUD Operations)
    │   │       ├── util/
    │   │       │   └── HibernateUtil.java    (Singleton SessionFactory)
    │   │       └── TestCarManagement.java    (Main Test)
    │   └── resources/
    │       └── hibernate.cfg.xml             (Configuration)
    └── test/
        └── java/
            └── (Unit tests)
```

## 🛠️ Công Nghệ Sử Dụng

| Công Nghệ | Phiên Bản |
|-----------|---------|
| Java | 11+ |
| Hibernate | 5.6.9 |
| MySQL | 8.0.32 |
| C3P0 | 0.9.5.5 |
| SLF4J | 1.7.36 |
| JUnit | 4.13.2 |

## 📋 Entity Models

### 1. Car (Xe)

```
Car
├── carId (PK)
├── carName (String, not null)
├── model (String)
├── yearOfProduction (int)
├── licensePlate (String, unique)
├── rentalPrice (double)
├── carStatus (Enum: AVAILABLE, RENTING, MAINTENANCE)
├── producer (N:1 FK to CarProducer)
├── createdDate (LocalDateTime)
└── updatedDate (LocalDateTime)
```

**Validation:**
- `yearOfProduction` ∈ [1900, năm hiện tại]
- `rentalPrice > 0`
- `licensePlate` duy nhất

**Trạng thái xe:**
- `AVAILABLE` - Có sẵn cho thuê
- `RENTING` - Đang được cho thuê
- `MAINTENANCE` - Đang bảo dưỡng

### 2. CarProducer (Hãng Sản Xuất)

```
CarProducer
├── producerId (PK)
├── producerName (String, unique)
├── country (String)
├── foundedYear (int)
├── description (TEXT)
├── createdDate (LocalDateTime)
└── cars (1:N to Car, cascade=ALL)
```

**Validation:**
- `producerName` không null
- `foundedYear` ∈ [1800, năm hiện tại]

## 📝 Repository Methods

### CarRepository

#### CREATE (Thêm)
```java
boolean save(Car car)                    // Thêm xe mới
```

#### READ (Lấy)
```java
Car findById(int carId)                  // Lấy xe theo ID
List<Car> findAll()                      // Lấy tất cả xe
Car findByLicensePlate(String plate)     // Lấy xe theo biển số
List<Car> findByName(String name)        // Tìm xe theo tên (LIKE)
List<Car> findByStatus(CarStatus status) // Lấy xe theo trạng thái
List<Car> getAvailableCars()             // Lấy xe có sẵn
List<Car> findByProducer(CarProducer p)  // Lấy xe theo hãng
List<Car> findByPriceRange(min, max)     // Tìm xe theo khoảng giá
```

#### UPDATE (Sửa)
```java
boolean update(Car car)                  // Cập nhật xe
boolean changeStatus(int id, status)     // Đổi trạng thái xe
```

#### DELETE (Xóa)
```java
boolean delete(int carId)                // Xóa xe (nếu AVAILABLE)
```

#### UTILITY (Tiện ích)
```java
long count()                             // Đếm tổng số xe
long countByStatus(CarStatus status)     // Đếm xe theo trạng thái
```

## ⚙️ Cấu Hình Hibernate

**File:** `src/main/resources/hibernate.cfg.xml`

```xml
<!-- Database Connection -->
<property name="hibernate.connection.url">
    jdbc:mysql://localhost:3306/car_rental_db
</property>
<property name="hibernate.connection.username">root</property>
<property name="hibernate.connection.password">123456</property>

<!-- Connection Pool (C3P0) -->
<property name="hibernate.c3p0.min_size">5</property>
<property name="hibernate.c3p0.max_size">20</property>

<!-- DDL Strategy -->
<property name="hibernate.hbm2ddl.auto">update</property>
```

## 🚀 Chạy Ứng Dụng

### 1. Cài đặt Dependencies
```bash
mvn clean install
```

### 2. Tạo Database
```sql
CREATE DATABASE car_rental_db CHARACTER SET utf8mb4;
```

### 3. Chạy Test
```bash
mvn exec:java -Dexec.mainClass="member3.TestCarManagement"
```

### 4. Output Kỳ Vọng
```
============================================================
🚗 TEST CAR MANAGEMENT SYSTEM 🚗
============================================================

1️⃣ THÊM XE:
------------------------------------------------------------
✅ Thêm xe thành công: Toyota Vios
✅ Thêm xe thành công: Honda Civic
✅ Thêm xe thành công: Toyota Camry

2️⃣ DANH SÁCH TẤT CẢ XE:
------------------------------------------------------------
  Car{id=1, name='Toyota Vios', model='Vios', year=2021, plate='29A-12345', price=300000, status=AVAILABLE, producer=Toyota}
  Car{id=2, name='Honda Civic', model='Civic', year=2020, plate='29A-54321', price=400000, status=AVAILABLE, producer=Honda}
  Car{id=3, name='Toyota Camry', model='Camry', year=2022, plate='29A-11111', price=500000, status=AVAILABLE, producer=Toyota}

...
```

## 🔧 Xử Lý Lỗi

### Lỗi: "Access denied for user 'root'@'localhost'"
**Giải pháp:** Cập nhật mật khẩu MySQL trong `hibernate.cfg.xml`
```xml
<property name="hibernate.connection.password">YOUR_PASSWORD</property>
```

### Lỗi: "No suitable driver found"
**Giải pháp:** Đảm bảo MySQL JDBC driver được thêm trong `pom.xml`

## ✨ Tính Năng Chính

✅ **CRUD Operations:** Thêm, sửa, xóa, lấy xe  
✅ **Search & Filter:** Tìm theo tên, biển số, giá, trạng thái  
✅ **Status Management:** Quản lý trạng thái xe (Available, Renting, Maintenance)  
✅ **Validation:** Kiểm tra hợp lệ dữ liệu  
✅ **Connection Pooling:** C3P0 cho hiệu suất tối ưu  
✅ **Transaction Management:** Rollback tự động khi lỗi  
✅ **Relationship:** N:1 quan hệ giữa Car và CarProducer  

## 📚 Tài Liệu Tham Khảo

- [Hibernate Documentation](https://hibernate.org/orm/documentation/5.6/)
- [MySQL JDBC Driver](https://dev.mysql.com/downloads/connector/j/)
- [C3P0 Connection Pool](https://www.mchange.com/projects/c3p0/)

## 👨‍💻 Tác Giả

Member3 - Lê Tuấn Anh

## 📄 License

MIT License
