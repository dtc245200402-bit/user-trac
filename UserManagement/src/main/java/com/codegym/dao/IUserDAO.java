package com.codegym.dao;

import com.codegym.model.User;
import java.sql.SQLException;
import java.util.List;

public interface IUserDAO {
    void insertUser(User user) throws Exception;
    User selectUser(int id);
    List<User> selectAllUsers();
    List<User> searchByCountry(String country);
    List<User> sortByName();
    boolean deleteUser(int id) throws Exception;
    boolean updateUser(User user) throws Exception;

    // Bài tập Stored Procedure + JDBC CallableStatement
    List<User> selectAllUsersByProcedure() throws SQLException;
    boolean updateUserByProcedure(User user) throws SQLException;
    boolean deleteUserByProcedure(int id) throws SQLException;

    void insertUpdateWithoutTransaction() throws SQLException;
    void insertUpdateUseTransaction() throws SQLException;
    void addUserTransaction() throws SQLException;
}
