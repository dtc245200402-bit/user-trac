# UserManagement - MySQL JDBC Transaction

Project JSP/Servlet/JDBC/MySQL tiếp tục từ các bài User Management trước.

## Bài tập hiện tại
Thêm phương thức `addUserTransaction()` để luyện tập `Connection.setAutoCommit(false)`, `commit()` và `rollback()`.

## Test rollback
1. Cấu hình tài khoản MySQL trong `UserDAO.java`.
2. Chạy project trên Tomcat 10.1+.
3. Truy cập:

`http://localhost:8080/user-management/users?action=test-add-user-transaction`

Phương thức cố tình INSERT vào bảng không tồn tại `users_transaction_error`. Hai User đầu tiên được INSERT trong cùng Transaction, sau đó lỗi SQL xảy ra và `rollback()` được gọi, vì vậy hai bản ghi đầu tiên cũng không còn trong database.

## Test commit
Trong `UserDAO.java`, đổi bảng `users_transaction_error` thành `users`. Build lại bằng:

```bash
mvn clean package
```

Sau đó chạy lại URL trên. Khi không còn lỗi, `commit()` được gọi và các bản ghi được lưu.

## MySQL kiểm tra
```sql
USE demo;
SELECT * FROM users;
```
