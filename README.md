# Hướng dẫn Setup cho Team

Để chạy được dự án này, máy các ông cần cài:

1. **Java:** - Cài JDK 11 (để chạy App).
   - Cài JDK 21 (để VS Code không báo lỗi).
2. **Maven:** Tải Maven về máy (nếu VS Code lỗi thì trỏ đường dẫn vào).
3. **SceneBuilder:** Tải và cài đặt SceneBuilder mới nhất.
4. **VS Code Extensions:** Khi mở project, nó hỏi cài extension thì bấm "Install All" nhé.

**Cách chạy:**
- Mở Menu Maven bên trái -> Plugins -> javafx -> javafx:run

# Musical_Store_Management_App

1. App mình là một cái desktop app, dành cho employee/staff. 
2. UI:
  - GUI 
    - Nếu bị dí thì dùng Java Swing 
    - Nếu không bị dí thì dùng JavaFX
3. App này có chức năng:
  - Quản lí các mặt hàng (instrument hoặc accessory)
  - Quản lí và theo dõi nhân viên
  - Thu thập thông tin cơ bản của khách hàng để tạo ra ORDER
  - Khả năng chỉnh sửa mặt hàng (thêm/xóa/sửa object) HOẶC tin chỉnh hệ thống lâu dài
  - ...
4. Database:
  - Xài filehandling

---------------------------------------------------------------------------------------------

Class InventoryManager
  Responsibility:
    - Store a list of Product
    - GET information of a specific product (price, quantity,...)
    - Note: this class does not change/alter the value of Product objects
    - Store database as FileHandling
  Attributes:
    - A private list of Product
    - A private final file containing database
  Methods:
    - constructor + loadData()
    - add a new item
    - get all items
    - find item using its ID
    - load data (from a database to the list of Product)
    - save data (write to text file database)


---------------------------------------------------------------------------------------------

Class Product
  Responsibility:
    - Abstract for the layers Instrument & Accessory
  Attributes:
    - id, name, brand, sellingPrice, quantityStock
  Methods:
    - get(), set()
    - checkStock() check for the availability of the item.

---------------------------------------------------------------------------------------------

Class Instrument
  Responsibility:
    - Abstract for the layers Guitar, Piano, ...
  Attributes:
    - type, color, isElectric
  Methods:
    - get(), set()

---------------------------------------------------------------------------------------------

Class Accessory
  Responsibility:
  Attributes:
    - Type (accessories of ... (instru))
    - color, isElectric
  Methods:
    - get(), set()






Tóm tắt Mô hình Thiết kế (Entity Model)
1. Product.java (Abstract Class): Chứa id, price, stock.
2. Instrument.java (Abstract Class) extends Product: Chứa các thuộc tính riêng của nhạc cụ (ví dụ: type, numberOfStrings—nếu bạn đẩy xuống lớp con).
3. Accessory.java (Concrete Class) extends Product: Chứa các thuộc tính riêng của phụ kiện (ví dụ: material, compatibility).
4. InventoryManager.java (Service Class): Chứa List<Product> để quản lý tất cả các mặt hàng một cách thống nhất.

Class INHERITANCE
  Attributes:

  Methods:
    - constructor (tạo object STOCK mới; yêu cầu tất cả attribute phải điền đủ)
      tạo constructor mặc định (STOCK rỗng) và constructor có tham số
    - get_all() / làm thêm hàm get cho từng attribute 
    - set_all(nameStk, cateStk,..., priceStk) / (làm thêm hàm set cho từng attribute)


Lớp Product (Class Cha cao nhất) nên chứa tất cả các thuộc tính và hành vi chung cho mọi thứ trong cửa hàng:Thuộc tính (States)Kiểu dữ liệuLý doidStringID duy nhất để quản lý kho hàng.nameStringTên chung (ví dụ: "Dây đàn Elixir").brandStringThương hiệu.sellingPricedoubleGiá bán ra (cốt lõi của kinh doanh).quantityInStockintSố lượng tồn kho (cốt lõi của quản lý kho).Phương thức (Behaviors)Vai tròGhi chúpublic abstract String getDescription()Bắt buộc phải được triển khai bởi tất cả các lớp con.Đa hình.public boolean checkStock()Kiểm tra còn hàng hay không.Dùng chung cho mọi sản phẩm.


