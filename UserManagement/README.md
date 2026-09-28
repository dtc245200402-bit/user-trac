# User Management - JDBC Transaction thực hành

Project JSP/Servlet/JDBC/MySQL trên Tomcat 10.1+.

## Chức năng
- CRUD User
- Tìm kiếm theo country
- Sắp xếp theo name
- Thực hành SQL không sử dụng Transaction

## Bài test không Transaction
Truy cập:
`http://localhost:8080/user-management/users?action=test-without-tran`

Phương thức `insertUpdateWithoutTransaction()` sẽ:
1. Xóa và tạo lại bảng Employee.
2. Insert Quynh salary 10.
3. Insert Ngan salary 20.
4. Cố tình gây lỗi tại UPDATE vì không gán tham số 1.

Kết quả mong đợi: lỗi `No value specified for parameter 1`, nhưng hai dòng INSERT vẫn tồn tại vì không dùng Transaction/rollback.

Kiểm tra bằng:
`SELECT * FROM Employee;`

## Cấu hình MySQL
Sửa `jdbcUsername` và `jdbcPassword` trong `UserDAO.java`.

## Build
```bash
mvn clean package
```

Deploy `target/user-management.war` lên Tomcat 10.1+.


## Bài thực hành: SQL có sử dụng Transaction

Project đã bổ sung phương thức `insertUpdateUseTransaction()` và URL test:

`http://localhost:8080/user-management/users?action=test-use-tran`

Lần chạy đầu: mã UPDATE cố tình sai `setBigDecimal(2, ...)`, vì vậy Transaction không commit và dữ liệu INSERT được rollback.

Để kiểm tra trường hợp thành công, sửa dòng:

`psUpdate.setBigDecimal(2, new BigDecimal("999.99"));`

thành:

`psUpdate.setBigDecimal(1, new BigDecimal("999.99"));`

Sau đó build lại bằng `mvn clean package`, deploy lại WAR và chạy URL test. Khi đó Quynh có salary 999.99 và Ngan có salary 20.
