# Dictionary for objects' attributes and methods

## Attributes

| **Class** | **Attribute** | **Datatype** | **Definition** |
|-----------|---------------|--------------|----------------|
| **PRODUCT** | | | |
| Product | ID | String | Identity Digit for product |
| Product | nameStk | String | Name of product |
| Product | cateStk | String (Implement by switch case) | Category of product |
| Product | oriStk | String | Origin of product |
| Product | brandStk | String | Brand |
| Product | quaStk | String | Quantity of Stock |
| Product | importDate | java.time.LocalDate | Import date |
| Product | priceStk | double | Price |
| **INSTRUMENT** | | | |
| Instrument | cateIns | String (Implement by switch case) | Category of instrument |
| Instrument | mateIns | String | Material of instrument |
| Instrument | color | String | Color of instrument |
| Instrument | isElectric | boolean | Electric of Acoustic |
| **ACCESSORY | | | |
| Accessory | cateAcc | String | Category of accessory |
| Accessory | mateAcc | String | Material if accessory |
| Accessory | colorAcc | String | Color of accessory |
| Accessory | compatibleWith | String (Implement by switch case) | Compatible with keyboard/guitar/...? |
| **GUITAR** | | | |
| Guitar | cateGui | String | Category of guitar |
| Guitar | strNumGui | int | Number of strings |
| Guitar | bodyshapeGui | String | Type of shape |
| **PIPANO** | | | |
| Piano | catePi | String | Category of piano |
| Piano | keyNumPi | int | Number of keys |
| Piano | hasPedal | boolean | Has pedal or not |
| **KEYBOARD** | | | |
| Keyboard | cateKey | String | Category of keyboard |
| Keyboard | keyNumKey | int | Number of keys |
| Keyboard | hasLCD | boolean | Has LCD Screen or not |
| **DRUM** | | | |
| Drum | cateDr | String | Category of drum |
| Drum | hasCymbal | boolean | Has cymbal or not |
| Drum | hasTripod | boolean | Has tripod or not |
| Drum | pieceNumDr | int | number of pieces (if electric) |
| **CUSTOMER** | | | |
| Customer | nameCus | String | Name of customer |
| Customer | CSN | String | Customer Service Number |
| Customer | phoneNum | String | Phone number of customer |
| Customer | emailCus | String | Email of customer |
| Customer | addCus | String | Address of Customer |
| **EMPLOYEE** | | | |
| Employee | nameEmp | String | Name of employee |
| Employee | EID | String | ID of employee |
| Employee | posEmp | String | Position |
| Employee | salEmp | Salary of Employee |
| Employee | hireDate | java.time.LocalDate | Date of hiring |
| **ORDER** | | | |
| Order | status | String | Payment status |
| Order | sellDate | java.time.LocalDate | Selling date |
| Order | deliDate | java.time.LocalDate | Delivering date |
| Order | deliAdd | String | Delivering address |
