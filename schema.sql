-- ==================================================
-- 1. DATABASE SETUP (RESET)
-- ==================================================
DROP DATABASE IF EXISTS musical_store_db;
CREATE DATABASE musical_store_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE musical_store_db;

-- ==================================================
-- 2. TABLE CREATION
-- ==================================================

-- A. SẢN PHẨM & KHO
CREATE TABLE Product (
    id VARCHAR(10) PRIMARY KEY, 
    namePro VARCHAR(255) NOT NULL, 
    catePro VARCHAR(50) NOT NULL, 
    origin VARCHAR(100), 
    brand VARCHAR(100), 
    quantityInStock INT NOT NULL, 
    importDate DATE, 
    sellingPrice DOUBLE NOT NULL
);

CREATE TABLE Instrument (
    product_id VARCHAR(10) PRIMARY KEY, 
    cateIns VARCHAR(50) NOT NULL, 
    mateIns VARCHAR(100), 
    colorIns VARCHAR(50), 
    isElectric BOOLEAN, 
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

-- Bảng chi tiết từng loại
CREATE TABLE GuitarDetail (
    product_id VARCHAR(10) PRIMARY KEY, cateGui VARCHAR(100), strNumGui INT, bodyShapeGui VARCHAR(100), 
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);
CREATE TABLE PianoDetail (
    product_id VARCHAR(10) PRIMARY KEY, catePi VARCHAR(100), keyNumPi INT, hasPedal BOOLEAN, 
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);
CREATE TABLE KeyboardDetail (
    product_id VARCHAR(10) PRIMARY KEY, cateKey VARCHAR(100), keyNumKey INT, hasLCD BOOLEAN, 
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);
CREATE TABLE DrumKitDetail (
    product_id VARCHAR(10) PRIMARY KEY, numOfDrumPieces INT, numOfCymbals INT, headMaterial VARCHAR(100), shellMaterial VARCHAR(100), 
    FOREIGN KEY (product_id) REFERENCES Instrument(product_id) ON DELETE CASCADE
);
CREATE TABLE AccessoryDetail (
    product_id VARCHAR(10) PRIMARY KEY, cateAcc VARCHAR(100), mateAcc VARCHAR(100), colorAcc VARCHAR(50), compatibleWith VARCHAR(100), 
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE CASCADE
);

-- B. CON NGƯỜI & TÀI KHOẢN
CREATE TABLE Customer (
    csn VARCHAR(20) PRIMARY KEY, 
    nameCus VARCHAR(255) NOT NULL, 
    phoneNum VARCHAR(20), 
    emailCus VARCHAR(100), 
    addCus VARCHAR(255)
);

CREATE TABLE Employee (
    eid VARCHAR(20) PRIMARY KEY, 
    nameEmp VARCHAR(255) NOT NULL, 
    posEmp VARCHAR(50), 
    salEmp INT, 
    hireDate DATE
);

-- [ĐÃ SỬA] Thêm liên kết với Employee để biết tài khoản nào của ai
CREATE TABLE Account (
    username VARCHAR(50) PRIMARY KEY, 
    password VARCHAR(50) NOT NULL, 
    role VARCHAR(20) NOT NULL,
    employee_eid VARCHAR(20) UNIQUE, -- Một nhân viên chỉ có 1 tài khoản
    FOREIGN KEY (employee_eid) REFERENCES Employee(eid) ON DELETE CASCADE
);

-- C. ĐƠN HÀNG (ORDER TRANSACTION)
CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT, 
    status VARCHAR(50), 
    sellDate DATE, 
    deliDate DATE, 
    deliAdd VARCHAR(255), 
    customer_csn VARCHAR(20),
    employee_eid VARCHAR(20), -- [ĐÃ SỬA] Thêm cột để biết nhân viên nào bán
    FOREIGN KEY (customer_csn) REFERENCES Customer(csn) ON DELETE SET NULL,
    FOREIGN KEY (employee_eid) REFERENCES Employee(eid) ON DELETE SET NULL
);

CREATE TABLE OrderDetail (
    order_detail_id INT PRIMARY KEY AUTO_INCREMENT, 
    order_id INT NOT NULL, 
    product_id VARCHAR(10) NOT NULL, 
    quantity INT NOT NULL, 
    price_at_sale DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE, 
    FOREIGN KEY (product_id) REFERENCES Product(id) ON DELETE NO ACTION
);

-- ==================================================
-- 3. DATA SEEDING (DỮ LIỆU MẪU)
-- ==================================================

-- A. NHÂN VIÊN & TÀI KHOẢN TEAM
-- [ĐÃ SỬA] Map tài khoản với đúng mã nhân viên (E001, E002, E003)

-- 1. Phan Anh Minh (Manager)
INSERT INTO Employee VALUES ('E001', 'Phan Anh Minh', 'Manager', 5000, '2023-01-01');
INSERT INTO Account VALUES ('minh', '10423191', 'Manager', 'E001');

-- 2. Le Ngoc Khang Duy (Staff)
INSERT INTO Employee VALUES ('E002', 'Le Ngoc Khang Duy', 'Staff', 3500, '2023-05-15');
INSERT INTO Account VALUES ('duy', '10423024', 'Staff', 'E002');

-- 3. Le Tri Dung (Staff)
INSERT INTO Employee VALUES ('E003', 'Le Tri Dung', 'Staff', 3500, '2023-06-20');
INSERT INTO Account VALUES ('dung', '10423022', 'Staff', 'E003');


-- B. KHÁCH HÀNG (Giữ nguyên)
INSERT INTO Customer VALUES ('C001', 'Nguyen Van A', '0901234567', 'vana@gmail.com', 'District 1, HCMC');
INSERT INTO Customer VALUES ('C002', 'Tran Thi B', '0912345678', 'bibi@gmail.com', 'Hanoi City');
INSERT INTO Customer VALUES ('C003', 'Le Van C', '0988888888', 'vip@gmail.com', 'Danang City');
INSERT INTO Customer VALUES ('C004', 'Pam Yeu Oi', '0999999999', 'pam@gmail.com', 'Vinhome Grand Park');
INSERT INTO Customer VALUES ('C005', 'John Wick', '0911911911', 'john@continental.com', 'Unknown Location');
INSERT INTO Customer VALUES ('C006', 'Tony Stark', '0123456789', 'ironman@avengers.com', 'Stark Tower, NYC');
INSERT INTO Customer VALUES ('C007', 'Doreamon', '0000000000', 'cat@future.jp', 'Tokyo, Japan');
INSERT INTO Customer VALUES ('C008', 'Sontung MTP', '0977777777', 'mtp@sky.vn', 'Thai Binh, Vietnam');
INSERT INTO Customer VALUES ('C009', 'Hieuthuhai', '0966666666', 'hieu@rapper.vn', 'Ho Chi Minh City');
INSERT INTO Customer VALUES ('C010', 'Mono', '0955555555', 'mono@onion.vn', 'Ho Chi Minh City');


-- C. SẢN PHẨM (Giữ nguyên 100%)

-- --- GUITARS ---
INSERT INTO Product VALUES ('111001', 'Fender Stratocaster', 'Instrument', 'USA', 'Fender', 10, '2025-01-15', 1500.0);
INSERT INTO Instrument VALUES ('111001', 'guitar', 'Alder', 'Sunburst', 1);
INSERT INTO GuitarDetail VALUES ('111001', 'Electric', 6, 'Stratocaster');

INSERT INTO Product VALUES ('111002', 'Gibson Les Paul Standard', 'Instrument', 'USA', 'Gibson', 5, '2025-04-12', 2500.0);
INSERT INTO Instrument VALUES ('111002', 'guitar', 'Mahogany', 'Gold Top', 1);
INSERT INTO GuitarDetail VALUES ('111002', 'Electric', 6, 'Single Cut');

INSERT INTO Product VALUES ('111003', 'Ibanez RG550', 'Instrument', 'Japan', 'Ibanez', 12, '2025-06-20', 999.0);
INSERT INTO Instrument VALUES ('111003', 'guitar', 'Basswood', 'Neon Purple', 1);
INSERT INTO GuitarDetail VALUES ('111003', 'Electric', 6, 'Superstrat');

INSERT INTO Product VALUES ('111004', 'Taylor 314ce', 'Instrument', 'USA', 'Taylor', 8, '2025-07-05', 2199.0);
INSERT INTO Instrument VALUES ('111004', 'guitar', 'Sapele', 'Natural', 0);
INSERT INTO GuitarDetail VALUES ('111004', 'Acoustic', 6, 'Grand Auditorium');

INSERT INTO Product VALUES ('111005', 'Martin D-28', 'Instrument', 'USA', 'Martin', 4, '2025-08-10', 3199.0);
INSERT INTO Instrument VALUES ('111005', 'guitar', 'Rosewood', 'Natural', 0);
INSERT INTO GuitarDetail VALUES ('111005', 'Acoustic', 6, 'Dreadnought');

-- --- PIANOS ---
INSERT INTO Product VALUES ('112001', 'Yamaha U1 Upright', 'Instrument', 'Japan', 'Yamaha', 3, '2025-02-10', 5000.0);
INSERT INTO Instrument VALUES ('112001', 'piano', 'Spruce', 'Polished Ebony', 0);
INSERT INTO PianoDetail VALUES ('112001', 'Upright', 88, 1);

INSERT INTO Product VALUES ('112002', 'Kawai K-300', 'Instrument', 'Japan', 'Kawai', 5, '2025-09-01', 6500.0);
INSERT INTO Instrument VALUES ('112002', 'piano', 'Mahogany', 'Black', 0);
INSERT INTO PianoDetail VALUES ('112002', 'Upright', 88, 1);

INSERT INTO Product VALUES ('112003', 'Steinway Model D', 'Instrument', 'Germany', 'Steinway', 1, '2025-10-15', 150000.0);
INSERT INTO Instrument VALUES ('112003', 'piano', 'Maple', 'Black', 0);
INSERT INTO PianoDetail VALUES ('112003', 'Grand', 88, 1);

INSERT INTO Product VALUES ('112004', 'Roland HP704', 'Instrument', 'Malaysia', 'Roland', 10, '2025-10-20', 2500.0);
INSERT INTO Instrument VALUES ('112004', 'piano', 'Wood/Plastic', 'Dark Rosewood', 1);
INSERT INTO PianoDetail VALUES ('112004', 'Digital Upright', 88, 1);

-- --- KEYBOARDS ---
INSERT INTO Product VALUES ('113001', 'Roland XPS-30', 'Instrument', 'Japan', 'Roland', 15, '2025-03-20', 900.0);
INSERT INTO Instrument VALUES ('113001', 'keyboard', 'Plastic', 'Black', 1);
INSERT INTO KeyboardDetail VALUES ('113001', 'Synth', 61, 1);

INSERT INTO Product VALUES ('113002', 'Nord Stage 3', 'Instrument', 'Sweden', 'Nord', 6, '2025-11-05', 4500.0);
INSERT INTO Instrument VALUES ('113002', 'keyboard', 'Metal', 'Red', 1);
INSERT INTO KeyboardDetail VALUES ('113002', 'Stage Piano', 88, 1);

INSERT INTO Product VALUES ('113003', 'Korg Kronos 2', 'Instrument', 'Japan', 'Korg', 5, '2025-11-12', 3300.0);
INSERT INTO Instrument VALUES ('113003', 'keyboard', 'Metal', 'Black', 1);
INSERT INTO KeyboardDetail VALUES ('113003', 'Workstation', 73, 1);

INSERT INTO Product VALUES ('113004', 'Yamaha PSR-SX900', 'Instrument', 'Indonesia', 'Yamaha', 20, '2025-03-25', 1400.0);
INSERT INTO Instrument VALUES ('113004', 'keyboard', 'Plastic', 'Black', 1);
INSERT INTO KeyboardDetail VALUES ('113004', 'Arranger', 61, 1);

-- --- DRUMS ---
INSERT INTO Product VALUES ('114001', 'Pearl Roadshow', 'Instrument', 'China', 'Pearl', 8, '2025-03-05', 650.0);
INSERT INTO Instrument VALUES ('114001', 'drumkit', 'Poplar', 'Wine Red', 0);
INSERT INTO DrumKitDetail VALUES ('114001', 5, 2, 'Mylar', 'Poplar');

INSERT INTO Product VALUES ('114002', 'Tama Starclassic', 'Instrument', 'Japan', 'Tama', 4, '2025-12-01', 2800.0);
INSERT INTO Instrument VALUES ('114002', 'drumkit', 'Walnut/Birch', 'Molten Blue', 0);
INSERT INTO DrumKitDetail VALUES ('114002', 5, 0, 'Evans', 'Walnut');

INSERT INTO Product VALUES ('114003', 'Roland TD-27KV', 'Instrument', 'Malaysia', 'Roland', 6, '2025-12-15', 3000.0);
INSERT INTO Instrument VALUES ('114003', 'drumkit', 'Mesh/Rubber', 'Black', 1);
INSERT INTO DrumKitDetail VALUES ('114003', 5, 3, 'Mesh', 'Plastic');

-- --- ACCESSORIES ---
INSERT INTO Product VALUES ('115001', 'Ernie Ball Strings', 'Accessory', 'USA', 'Ernie Ball', 200, '2025-01-20', 10.0);
INSERT INTO AccessoryDetail VALUES ('115001', 'Strings', 'Nickel', 'Silver', 'Electric Guitar');

INSERT INTO Product VALUES ('115002', 'Hercules Guitar Stand', 'Accessory', 'China', 'Hercules', 100, '2025-05-05', 25.0);
INSERT INTO AccessoryDetail VALUES ('115002', 'Stand', 'Metal', 'Black', 'Guitar/Bass');

INSERT INTO Product VALUES ('115003', 'Mogami Gold Cable', 'Accessory', 'Japan', 'Mogami', 50, '2025-02-15', 50.0);
INSERT INTO AccessoryDetail VALUES ('115003', 'Cable', 'Copper', 'Black', 'Instruments');

INSERT INTO Product VALUES ('115004', 'Vic Firth 5A', 'Accessory', 'USA', 'Vic Firth', 150, '2025-04-10', 12.0);
INSERT INTO AccessoryDetail VALUES ('115004', 'Drumsticks', 'Hickory', 'Wood', 'Drums');

INSERT INTO Product VALUES ('115005', 'Boss DS-1 Distortion', 'Accessory', 'Taiwan', 'Boss', 30, '2025-07-20', 60.0);
INSERT INTO AccessoryDetail VALUES ('115005', 'Pedal', 'Metal', 'Orange', 'Electric Guitar');

INSERT INTO Product VALUES ('115006', 'Marshall DSL40CR', 'Accessory', 'Vietnam', 'Marshall', 10, '2025-08-10', 750.0);
INSERT INTO AccessoryDetail VALUES ('115006', 'Amplifier', 'Tolex', 'Black', 'Electric Guitar');