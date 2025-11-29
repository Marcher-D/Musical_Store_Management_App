-- Tệp này chứa mã SQL để XÓA và TẠO lại toàn bộ cấu trúc database.

-- 1. XÓA VÀ TẠO LẠI DATABASE
DROP DATABASE IF EXISTS musical_store_db;
CREATE DATABASE musical_store_db;
USE musical_store_db;

-- Tệp SQL này được tạo dựa trên các hằng số và câu lệnh CREATE TABLE 
-- trong ProductDAO.java, CustomerDAO.java và EmployeeDAO.java.

-- ====================================================================
-- 1. CẤU HÌNH BAN ĐẦU
-- ====================================================================

-- Tên Database được định nghĩa trong các file Java
CREATE DATABASE IF NOT EXISTS musical_store_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE musical_store_db;

-- ====================================================================
-- 2. XOÁ CÁC BẢNG NẾU TỒN TẠI (Đảm bảo chạy sạch)
-- ====================================================================
-- Xóa các bảng chi tiết trước để tránh lỗi ràng buộc khoá ngoại (Foreign Key)
DROP TABLE IF EXISTS DrumKitDetail;
DROP TABLE IF EXISTS KeyboardDetail;
DROP TABLE IF EXISTS PianoDetail;
DROP TABLE IF EXISTS GuitarDetail;
DROP TABLE IF EXISTS AccessoryDetail;
DROP TABLE IF EXISTS Instrument;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Customer; -- Bổ sung DROP
DROP TABLE IF EXISTS Employee; -- Bổ sung DROP
DROP TABLE IF EXISTS Account; 

-- ====================================================================
-- 3. TẠO CÁC BẢNG CHÍNH VÀ CHI TIẾT
-- ====================================================================

-- Bảng 1: Product (Thông tin chung cho mọi sản phẩm)
CREATE TABLE Product (
    id VARCHAR(10) PRIMARY KEY,
    namePro VARCHAR(255) NOT NULL,
    catePro VARCHAR(50) NOT NULL, -- Category (e.g., 'Instrument', 'Accessory')
    origin VARCHAR(100),
    brand VARCHAR(100),
    quantityInStock INT NOT NULL,
    importDate DATE,
    sellingPrice DOUBLE NOT NULL
);

-- Bảng 2: Instrument (Chi tiết chung cho Nhạc cụ)
CREATE TABLE Instrument (
    product_id VARCHAR(10) PRIMARY KEY,
    cateIns VARCHAR(50) NOT NULL, -- Sub Category (e.g., 'Guitar', 'Piano', 'Drumkit')
    mateIns VARCHAR(100),
    colorIns VARCHAR(50),
    isElectric BOOLEAN,
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

-- Bảng 3: GuitarDetail (Chi tiết cụ thể cho Guitar)
CREATE TABLE GuitarDetail (
    product_id VARCHAR(10) PRIMARY KEY,
    cateGui VARCHAR(100),
    strNumGui INT,
    bodyShapeGui VARCHAR(100),
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);

-- Bảng 4: PianoDetail
CREATE TABLE PianoDetail (
    product_id VARCHAR(10) PRIMARY KEY,
    catePi VARCHAR(100),
    keyNumPi INT,
    hasPedal BOOLEAN,
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);

-- Bảng 5: KeyboardDetail
CREATE TABLE KeyboardDetail (
    product_id VARCHAR(10) PRIMARY KEY,
    cateKey VARCHAR(100),
    keyNumKey INT,
    hasLCD BOOLEAN,
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);

-- Bảng 6: DrumKitDetail
CREATE TABLE DrumKitDetail (
    product_id VARCHAR(10) PRIMARY KEY,
    numOfDrumPieces INT,
    numOfCymbals INT,
    headMaterial VARCHAR(100),
    shellMaterial VARCHAR(100),
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);

-- Bảng 7: AccessoryDetail (Chi tiết cụ thể cho Phụ kiện)
CREATE TABLE AccessoryDetail (
    product_id VARCHAR(10) PRIMARY KEY,
    cateAcc VARCHAR(100),
    mateAcc VARCHAR(100), 
    colorAcc VARCHAR(50),
    compatibleWith VARCHAR(100), 
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

-- Bảng 8: Account (Dùng cho chức năng Đăng nhập - LoginController/AccountDAO)
CREATE TABLE Account (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL -- Manager, Staff
);

-- Bảng 9: Customer (Dựa trên Customer.java)
CREATE TABLE Customer (
    CSN VARCHAR(50) PRIMARY KEY, -- Mã số Khách hàng (Sử dụng CSN làm khóa chính)
    nameCus VARCHAR(255) NOT NULL,
    phoneNum VARCHAR(20),
    emailCus VARCHAR(255),
    addCus VARCHAR(255)
);

-- Bảng 10: Employee (Dựa trên Employee.java)
CREATE TABLE Employee (
    EID VARCHAR(50) PRIMARY KEY, -- Mã số Nhân viên (Sử dụng EID làm khóa chính)
    nameEmp VARCHAR(255) NOT NULL,
    posEmp VARCHAR(100), -- Vị trí (e.g., Manager, Staff, IT)
    salEmp INT, -- Mức lương
    hireDate DATE -- Ngày tuyển dụng
);


-- ====================================================================
-- 4. INSERT DỮ LIỆU MẪU (TEST DATA)
-- ====================================================================

-- 4.1. Dữ liệu mẫu cho Đăng nhập
INSERT INTO Account (username, password, role) VALUES ('admin', '123', 'Manager');
INSERT INTO Account (username, password, role) VALUES ('staff', '123', 'Staff');

-- 4.2. Dữ liệu mẫu cho Employee
INSERT INTO Employee (EID, nameEmp, posEmp, salEmp, hireDate) VALUES 
('E001', 'Trần Văn Mạnh', 'Manager', 5000, '2020-01-15') ON DUPLICATE KEY UPDATE EID=EID, salEmp=5000;
INSERT INTO Employee (EID, nameEmp, posEmp, salEmp, hireDate) VALUES 
('E002', 'Nguyễn Thị Bích', 'Sales Staff', 1500, '2023-05-20') ON DUPLICATE KEY UPDATE EID=EID, salEmp=1500;
INSERT INTO Employee (EID, nameEmp, posEmp, salEmp, hireDate) VALUES 
('E003', 'Lê Hữu Dũng', 'Technician', 2200, '2022-11-01') ON DUPLICATE KEY UPDATE EID=EID, salEmp=2200;

-- 4.3. Dữ liệu mẫu cho Customer
INSERT INTO Customer (CSN, nameCus, phoneNum, emailCus, addCus) VALUES 
('C001', 'Phạm Anh Khoa', '0901234567', 'khoa.p@mail.com', '123 Đường Láng, Hà Nội') ON DUPLICATE KEY UPDATE CSN=CSN;
INSERT INTO Customer (CSN, nameCus, phoneNum, emailCus, addCus) VALUES 
('C002', 'Đào Thị Minh', '0987654321', 'minh.dao@mail.com', '456 Phố Huế, Hà Nội') ON DUPLICATE KEY UPDATE CSN=CSN;
INSERT INTO Customer (CSN, nameCus, phoneNum, emailCus, addCus) VALUES 
('C003', 'Vũ Hoàng Nam', '0912345678', 'nam.vu@mail.com', '789 Quận 1, TP.HCM') ON DUPLICATE KEY UPDATE CSN=CSN;


-- 4.4. Dữ liệu mẫu cho Product (Giữ nguyên ID, giá trị và cấu trúc ban đầu)

-- Sản phẩm 1: Guitar (ID: 111001)
SET @prod_id_g = '111001';
INSERT INTO Product (id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice)
VALUES (@prod_id_g, 'Fender Stratocaster Classic', 'Instrument', 'USA', 'Fender', 5, '2024-05-10', 1500.00)
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO Instrument (product_id, cateIns, mateIns, colorIns, isElectric)
VALUES (@prod_id_g, 'Guitar', 'Alder', 'Sunburst', 1)
ON DUPLICATE KEY UPDATE product_id=product_id;
INSERT INTO GuitarDetail (product_id, cateGui, strNumGui, bodyShapeGui)
VALUES (@prod_id_g, 'Electric', 6, 'Stratocaster')
ON DUPLICATE KEY UPDATE product_id=product_id;

-- Sản phẩm 2: Piano (ID: 112001)
SET @prod_id_p = '112001';
INSERT INTO Product (id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice)
VALUES (@prod_id_p, 'Yamaha Grand C7', 'Instrument', 'Japan', 'Yamaha', 1, '2023-12-01', 35000.00)
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO Instrument (product_id, cateIns, mateIns, colorIns, isElectric)
VALUES (@prod_id_p, 'Piano', 'Spruce/Maple', 'Black', 0)
ON DUPLICATE KEY UPDATE product_id=product_id;
INSERT INTO PianoDetail (product_id, catePi, keyNumPi, hasPedal)
VALUES (@prod_id_p, 'Grand', 88, 1)
ON DUPLICATE KEY UPDATE product_id=product_id;

-- Sản phẩm 3: Accessory (ID: 115001)
SET @prod_id_a = '115001';
INSERT INTO Product (id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice)
VALUES (@prod_id_a, 'D Addario EXL110 Strings', 'Accessory', 'USA', 'D Addario', 100, '2024-07-25', 10.50)
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO AccessoryDetail (product_id, cateAcc, mateAcc, colorAcc, compatibleWith)
VALUES (@prod_id_a, 'Strings', 'Nickel Plated Steel', 'Silver', 'Electric Guitar')
ON DUPLICATE KEY UPDATE product_id=product_id;

-- Sản phẩm 4: DrumKit (ID: 114001)
SET @prod_id_d = '114001';
INSERT INTO Product (id, namePro, catePro, origin, brand, quantityInStock, importDate, sellingPrice)
VALUES (@prod_id_d, 'Pearl Roadshow Drum Kit', 'Instrument', 'China', 'Pearl', 3, '2024-03-20', 650.00)
ON DUPLICATE KEY UPDATE id=id;
INSERT INTO Instrument (product_id, cateIns, mateIns, colorIns, isElectric)
VALUES (@prod_id_d, 'Drumkit', 'Poplar', 'Wine Red', 0)
ON DUPLICATE KEY UPDATE product_id=product_id;
INSERT INTO DrumKitDetail (product_id, numOfDrumPieces, numOfCymbals, headMaterial, shellMaterial)
VALUES (@prod_id_d, 5, 2, 'Mylar', 'Poplar Wood')
ON DUPLICATE KEY UPDATE product_id=product_id;