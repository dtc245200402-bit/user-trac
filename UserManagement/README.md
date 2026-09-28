# User Management - Stored Procedures + JDBC

Bài tập cập nhật ứng dụng quản lý User để gọi MySQL Stored Procedures bằng `CallableStatement`.

## Chức năng
- Hiển thị danh sách User bằng `get_all_users()`
- Sửa User bằng `update_user(...)`
- Xoá User bằng `delete_user(...)`
- Các chức năng tìm kiếm theo country và sắp xếp theo name của bài trước vẫn được giữ lại.
- Các bài Transaction trước vẫn được giữ lại để tiện học tập.

## 1. Tạo Stored Procedures
Mở MySQL Workbench/DBeaver và chạy toàn bộ file `database.sql`.

Ba procedure được tạo:
- `get_all_users()`
- `update_user(p_id, p_name, p_email, p_country)`
- `delete_user(p_id)`

## 2. Cấu hình MySQL
Mở `src/main/java/com/codegym/dao/UserDAO.java` và sửa:

```java
private String jdbcUsername = "root";
private String jdbcPassword = "password";
```

## 3. Chạy project
```bash
mvn clean package
```

Deploy `target/user-management.war` vào Tomcat 10.1+.

Truy cập:
`http://localhost:8080/user-management/users`

## 4. Kiểm tra
- Mở danh sách: Servlet gọi `get_all_users()` qua `CallableStatement`.
- Bấm Sửa và lưu: Servlet gọi `update_user(...)`.
- Bấm Xoá: Servlet gọi `delete_user(...)`.

Có thể kiểm tra procedure bằng MySQL:
```sql
USE demo;
CALL get_all_users();
```
