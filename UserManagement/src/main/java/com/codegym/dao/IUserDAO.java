package com.codegym.dao;

import com.codegym.model.User;
import java.util.List;
import java.sql.SQLException;

public interface IUserDAO {
    void insertUser(User user) throws Exception;
    User selectUser(int id);
    List<User> selectAllUsers();
    List<User> searchByCountry(String country);
    List<User> sortByName();
    boolean deleteUser(int id) throws Exception;
    boolean updateUser(User user) throws Exception;
    void insertUpdateWithoutTransaction() throws SQLException;
    void insertUpdateUseTransaction() throws SQLException;
}
