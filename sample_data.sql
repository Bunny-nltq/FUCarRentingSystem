-- Thêm dữ liệu mẫu cho Car Producer
INSERT INTO `carproducer` (`ProducerID`, `ProducerName`, `Address`, `Country`, `name`) VALUES
(1, 'Toyota Motor Corporation', 'Toyota City, Aichi', 'Japan', 'Toyota'),
(2, 'Honda Motor Co., Ltd.', 'Tokyo', 'Japan', 'Honda'),
(3, 'Hyundai Motor Company', 'Seoul', 'South Korea', 'Hyundai'),
(4, 'Ford Motor Company', 'Dearborn, Michigan', 'USA', 'Ford'),
(5, 'BMW Group', 'Munich', 'Germany', 'BMW');

-- Thêm dữ liệu mẫu cho Car
INSERT INTO `car` (`CarID`, `CarName`, `CarModelYear`, `Color`, `Capacity`, `Description`, `ImportDate`, `ProducerID`, `rentPrice`, `Status`, `LicensePlate`) VALUES
(1, 'Toyota Camry', 2023, 'White', 5, 'Sedan cao cấp, tiết kiệm nhiên liệu', '2023-01-15', 1, 500000, 'AVAILABLE', '30A-12345'),
(2, 'Honda CR-V', 2023, 'Black', 7, 'SUV gia đình, rộng rãi thoải mái', '2023-02-20', 2, 600000, 'AVAILABLE', '30B-67890'),
(3, 'Hyundai Tucson', 2022, 'Silver', 5, 'SUV hiện đại, công nghệ tiên tiến', '2022-12-10', 3, 550000, 'AVAILABLE', '30C-11111'),
(4, 'Ford Ranger', 2023, 'Blue', 5, 'Bán tải mạnh mẽ, địa hình tốt', '2023-03-05', 4, 700000, 'AVAILABLE', '30D-22222'),
(5, 'BMW X5', 2024, 'Gray', 5, 'SUV sang trọng, hiệu suất cao', '2024-01-10', 5, 1200000, 'AVAILABLE', '30E-33333'),
(6, 'Toyota Vios', 2023, 'Red', 5, 'Sedan phổ thông, kinh tế', '2023-04-20', 1, 400000, 'RENTED', '30F-44444'),
(7, 'Honda City', 2023, 'White', 5, 'Sedan đô thị, tiện nghi', '2023-05-15', 2, 450000, 'AVAILABLE', '30G-55555'),
(8, 'Hyundai Accent', 2022, 'Blue', 5, 'Sedan nhỏ gọn, tiết kiệm', '2022-11-01', 3, 420000, 'AVAILABLE', '30H-66666');

-- Thêm dữ liệu mẫu cho Car Rental
-- CustomerID 3 là Nhi (AccountID 5)
INSERT INTO `carrental` (`RentalID`, `CustomerID`, `CarID`, `PickupDate`, `ReturnDate`, `RentPrice`, `Status`, `CreatedAt`) VALUES
(1, 3, 6, '2025-11-20', '2025-11-25', 2500000, 'RENTED', '2025-11-19 10:30:00'),
(2, 3, 2, '2025-11-15', '2025-11-18', 1800000, 'RETURNED', '2025-11-14 14:20:00'),
(3, 3, 1, '2025-11-10', '2025-11-12', 1000000, 'COMPLETED', '2025-11-09 09:15:00');

-- CustomerID 4 là test1 (AccountID 8)
INSERT INTO `carrental` (`RentalID`, `CustomerID`, `CarID`, `PickupDate`, `ReturnDate`, `RentPrice`, `Status`, `CreatedAt`) VALUES
(4, 4, 3, '2025-11-22', '2025-11-28', 3300000, 'RENTED', '2025-11-21 11:00:00'),
(5, 4, 7, '2025-11-05', '2025-11-08', 1350000, 'COMPLETED', '2025-11-04 16:45:00');

-- CustomerID 6 là tri (AccountID 14)
INSERT INTO `carrental` (`RentalID`, `CustomerID`, `CarID`, `PickupDate`, `ReturnDate`, `RentPrice`, `Status`, `CreatedAt`) VALUES
(6, 6, 4, '2025-11-25', '2025-11-30', 3500000, 'PENDING', '2025-11-24 08:30:00'),
(7, 6, 5, '2025-11-01', '2025-11-03', 2400000, 'RETURNED', '2025-10-31 13:20:00');

-- Thêm dữ liệu mẫu cho Review
INSERT INTO `review` (`ReviewID`, `CustomerID`, `CarID`, `ReviewStar`, `Comment`, `CreatedAt`) VALUES
(1, 3, 2, 5, 'Xe rất tốt, sạch sẽ và thoải mái. Sẽ thuê lại!', '2025-11-18 20:30:00'),
(2, 3, 1, 4, 'Xe ổn, giá hợp lý. Chỉ hơi cũ một chút.', '2025-11-12 15:45:00'),
(3, 4, 7, 5, 'Dịch vụ tuyệt vời, xe mới và đẹp!', '2025-11-08 18:20:00'),
(4, 6, 5, 5, 'BMW X5 sang trọng, lái rất êm. Đáng đồng tiền!', '2025-11-03 22:10:00');
