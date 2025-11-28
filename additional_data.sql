-- Thêm dữ liệu bổ sung cho FUCarRentingSystem Database
-- Chạy file này sau khi đã có dữ liệu cơ bản

USE fucarrentingsystemdb;

-- Thêm Review cho các rental đã hoàn thành
-- Note: ReviewID 1-4 đã được sử dụng trong sample_data.sql, bắt đầu từ ID 5
INSERT INTO `review` (`ReviewID`, `CustomerID`, `CarID`, `ReviewStar`, `Comment`, `CreatedAt`) VALUES
(5, 3, 6, 4, 'Toyota Vios tiết kiệm nhiên liệu, lái êm. Phù hợp đi phố.', '2025-11-26 10:30:00'),
(6, 4, 3, 5, 'Hyundai Tucson đẹp và hiện đại, công nghệ tiên tiến. Rất hài lòng!', '2025-11-23 09:45:00'),
(7, 6, 4, 4, 'Ford Ranger mạnh mẽ, phù hợp địa hình. Hơi to nên khó đỗ xe trong thành phố.', '2025-11-26 14:15:00');

-- Thêm một số rental mới (nếu cần test thêm)
-- Note: Chỉ sử dụng CustomerID đã tồn tại: 3, 4, 6
INSERT INTO `carrental` (`CustomerID`, `CarID`, `PickupDate`, `ReturnDate`, `RentPrice`, `Status`, `CreatedAt`) VALUES
(3, 7, '2025-12-01', '2025-12-05', 1800000, 'PENDING', NOW()),
(4, 8, '2025-11-29', '2025-12-02', 1260000, 'PENDING', NOW()),
(6, 1, '2025-11-28', '2025-12-01', 1500000, 'PENDING', NOW());

-- Cập nhật trạng thái một số xe để có đa dạng
UPDATE `car` SET `Status` = 'AVAILABLE' WHERE `CarID` IN (1, 2, 3, 4, 5, 7, 8);
UPDATE `car` SET `Status` = 'RENTED' WHERE `CarID` = 6;

-- Đảm bảo có Admin account với password đúng (admin123)
-- Password hash của 'admin123' là: 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
UPDATE `account` 
SET `PasswordHash` = '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9'
WHERE `email` = 'admin@gmail.com';

-- Kiểm tra dữ liệu
SELECT 'Total Accounts:' as Info, COUNT(*) as Count FROM account
UNION ALL
SELECT 'Total Customers:', COUNT(*) FROM customer
UNION ALL
SELECT 'Total Cars:', COUNT(*) FROM car
UNION ALL
SELECT 'Total Car Producers:', COUNT(*) FROM carproducer
UNION ALL
SELECT 'Total Rentals:', COUNT(*) FROM carrental
UNION ALL
SELECT 'Total Reviews:', COUNT(*) FROM review;

-- Hiển thị một số dữ liệu quan trọng
SELECT '=== ADMIN ACCOUNTS ===' as Info;
SELECT AccountID, AccountName, email, Role FROM account WHERE Role = 'ADMIN';

SELECT '=== CUSTOMER ACCOUNTS ===' as Info;
SELECT AccountID, AccountName, email, Role FROM account WHERE Role = 'CUSTOMER' LIMIT 5;

SELECT '=== AVAILABLE CARS ===' as Info;
SELECT CarID, CarName, LicensePlate, Status, rentPrice FROM car WHERE Status = 'AVAILABLE' LIMIT 5;

SELECT '=== ACTIVE RENTALS ===' as Info;
SELECT 
    cr.RentalID,
    c.CustomerName,
    car.CarName,
    cr.PickupDate,
    cr.ReturnDate,
    cr.Status
FROM carrental cr
JOIN customer c ON cr.CustomerID = c.CustomerID
JOIN car ON cr.CarID = car.CarID
WHERE cr.Status IN ('PENDING', 'RENTED')
ORDER BY cr.CreatedAt DESC;

SELECT '=== RECENT REVIEWS ===' as Info;
SELECT 
    r.ReviewID,
    c.CustomerName,
    car.CarName,
    r.ReviewStar,
    r.Comment,
    r.CreatedAt
FROM review r
JOIN customer c ON r.CustomerID = c.CustomerID
JOIN car ON r.CarID = car.CarID
ORDER BY r.CreatedAt DESC
LIMIT 5;
