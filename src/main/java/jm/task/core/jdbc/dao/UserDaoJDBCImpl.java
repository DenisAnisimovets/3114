package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS predproekt.users" +
                    "(id mediumint not null auto_increment," +
                    " name VARCHAR(50), " +
                    "lastname VARCHAR(50), " +
                    "age tinyint, " +
                    "PRIMARY KEY (id))");
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void dropUsersTable() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate("Drop table if exists users");
            System.out.println("Таблица удалена");
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        String sql = "INSERT INTO users(name, lastname, age) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void removeUserById(long id) throws SQLException {
        try (PreparedStatement pstm = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            connection.setAutoCommit(false);
            pstm.setLong(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        }
        connection.commit();
        connection.setAutoCommit(true);
    }

    public List<User> getAllUsers() throws SQLException {

        List<User> allUser = new ArrayList<>();
        String sql = "SELECT id, name, lastName, age from users";

        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                allUser.add(user);
            }

        } catch (Exception e) {
            connection.rollback();
            e.printStackTrace();
        }
        connection.commit();
        connection.setAutoCommit(true);
        return allUser;
    }

    public void cleanUsersTable() throws SQLException {
        String sql = "DELETE FROM users";
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.executeUpdate(sql);
            System.out.println("Таблица очищена");
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
            System.out.println("Не удалось очистить");
        }
        connection.commit();
        connection.setAutoCommit(true);
    }
}
