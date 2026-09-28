package com.codegym.dao;

import com.codegym.model.User;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=UTC";
    private String jdbcUsername = "root";
    private String jdbcPassword = "password";

    private static final String INSERT = "INSERT INTO users(name,email,country) VALUES(?,?,?)";
    private static final String SELECT = "SELECT id,name,email,country FROM users WHERE id=?";
    private static final String ALL = "SELECT id,name,email,country FROM users ORDER BY id";
    private static final String SEARCH_COUNTRY = "SELECT id,name,email,country FROM users WHERE country LIKE ? ORDER BY id";
    private static final String SORT_NAME = "SELECT id,name,email,country FROM users ORDER BY name ASC";
    private static final String DELETE = "DELETE FROM users WHERE id=?";
    private static final String UPDATE = "UPDATE users SET name=?,email=?,country=? WHERE id=?";

    // Các câu lệnh phục vụ bài thực hành Transaction - không sử dụng Transaction
    private static final String SQL_INSERT = "INSERT INTO Employee (name, salary, created_Date) VALUES (?,?,?)";
    private static final String SQL_UPDATE = "UPDATE Employee SET salary=? WHERE name=?";
    private static final String SQL_TABLE_CREATE = "CREATE TABLE Employee"
            + "("
            + " id INT(11) AUTO_INCREMENT,"
            + " name VARCHAR(100) NOT NULL,"
            + " salary DECIMAL(15, 2) NOT NULL,"
            + " created_Date TIMESTAMP,"
            + " PRIMARY KEY (id)"
            + ")";
    private static final String SQL_TABLE_DROP = "DROP TABLE IF EXISTS Employee";

    protected Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(e);
        }
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    @Override
    public void insertUser(User u) throws Exception {
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(INSERT)) {
            p.setString(1, u.getName());
            p.setString(2, u.getEmail());
            p.setString(3, u.getCountry());
            p.executeUpdate();
        }
    }

    @Override
    public User selectUser(int id) {
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(SELECT)) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    return new User(r.getInt("id"), r.getString("name"),
                            r.getString("email"), r.getString("country"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<User> selectAllUsers() {
        return executeListQuery(ALL, null);
    }

    @Override
    public List<User> searchByCountry(String country) {
        return executeListQuery(SEARCH_COUNTRY, country == null ? "" : "%" + country.trim() + "%");
    }

    @Override
    public List<User> sortByName() {
        return executeListQuery(SORT_NAME, null);
    }

    private List<User> executeListQuery(String sql, String parameter) {
        List<User> list = new ArrayList<>();
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            if (parameter != null) {
                p.setString(1, parameter);
            }
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    list.add(new User(r.getInt("id"), r.getString("name"),
                            r.getString("email"), r.getString("country")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public boolean deleteUser(int id) throws Exception {
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(DELETE)) {
            p.setInt(1, id);
            return p.executeUpdate() > 0;
        }
    }

    /**
     * Thực hành nhiều câu SQL mà KHÔNG sử dụng Transaction.
     * Cố tình gây lỗi ở câu UPDATE để quan sát dữ liệu INSERT vẫn còn.
     */
    @Override
    public void insertUpdateWithoutTransaction() throws SQLException {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
             PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)) {

            // Mỗi lần test đều xoá và tạo lại bảng.
            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);

            // INSERT nhân viên Quynh.
            psInsert.setString(1, "Quynh");
            psInsert.setBigDecimal(2, new BigDecimal("10"));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            // INSERT nhân viên Ngan.
            psInsert.setString(1, "Ngan");
            psInsert.setBigDecimal(2, new BigDecimal("20"));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.execute();

            // CỐ TÌNH GÂY LỖI: tham số 1 của UPDATE không được gán.
            psUpdate.setBigDecimal(2, new BigDecimal("999.99"));
            // Đúng ra phải là: psUpdate.setBigDecimal(1, new BigDecimal("999.99"));
            psUpdate.setString(2, "Quynh");
            psUpdate.execute();

        } catch (SQLException e) {
            System.out.println("Đã bắt được lỗi trong quá trình thực thi SQL:");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Thực hành nhiều câu SQL có sử dụng JDBC Transaction.
     * Lần chạy đầu cố tình gây lỗi để kiểm tra rollback.
     * Sau khi sửa tham số UPDATE từ 2 thành 1, toàn bộ thao tác sẽ commit.
     */
    @Override
    public void insertUpdateUseTransaction() throws SQLException {
        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
             PreparedStatement psUpdate = conn.prepareStatement(SQL_UPDATE)) {

            statement.execute(SQL_TABLE_DROP);
            statement.execute(SQL_TABLE_CREATE);

            // Bắt đầu Transaction.
            conn.setAutoCommit(false);

            psInsert.setString(1, "Quynh");
            psInsert.setBigDecimal(2, new BigDecimal("10"));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.executeUpdate();

            psInsert.setString(1, "Ngan");
            psInsert.setBigDecimal(2, new BigDecimal("20"));
            psInsert.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            psInsert.executeUpdate();

            // CỐ TÌNH GÂY LỖI để kiểm tra rollback.
            // Đúng ra phải là psUpdate.setBigDecimal(1, new BigDecimal("999.99"));
            psUpdate.setBigDecimal(2, new BigDecimal("999.99"));
            psUpdate.setString(2, "Quynh");
            psUpdate.executeUpdate();

            // Chỉ khi mọi câu lệnh thành công mới commit.
            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            System.out.println("Lỗi xảy ra, Transaction sẽ được rollback:");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public boolean updateUser(User u) throws Exception {
        try (Connection c = getConnection(); PreparedStatement p = c.prepareStatement(UPDATE)) {
            p.setString(1, u.getName());
            p.setString(2, u.getEmail());
            p.setString(3, u.getCountry());
            p.setInt(4, u.getId());
            return p.executeUpdate() > 0;
        }
    }
}
