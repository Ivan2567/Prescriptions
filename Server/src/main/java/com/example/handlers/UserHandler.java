package com.example.handlers;

import java.sql.*;
import java.util.*;
import com.example.entities.User;
import jakarta.inject.Singleton;

//@Singleton
public class UserHandler {
    private static final String conn_s = "jdbc:postgresql://45.10.244.15:55532/work100016";
    private static UserHandler instance = null;
    public  static synchronized  UserHandler getInstance() throws SQLException{
        if(instance == null)
            instance = new UserHandler();
        return instance;
    }
    private Connection connection;
    private UserHandler() throws SQLException{
        //DriverManager.registerDriver(new JDBC() );
        try {Class.forName("org.postgresql.Driver");}
        catch (ClassNotFoundException e) {e.printStackTrace();}
        this.connection = DriverManager.getConnection(conn_s,"work100016","{FXcadFL99Ncvo?kOMW~");
    }
    public List<User> getAll() {
        try (Statement statement = this.connection.createStatement()) {
            List<User> users = new ArrayList<User>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");

            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("pass"),
                        resultSet.getString("role")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<User> checkUnique(String login) {
        try (Statement statement = this.connection.createStatement()) {
            String q = String.format("SELECT * FROM users WHERE role ='%s' ",login);
            List<User> users = new ArrayList<User>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("pass"),
                        resultSet.getString("role")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<User> checkLog(String login,String pass) {
        try (Statement statement = this.connection.createStatement()) {
            String q = String.format("SELECT * FROM users WHERE login = '%s' AND pass = '%s'",login,pass);
            List<User> users = new ArrayList<User>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("pass"),
                        resultSet.getString("role")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<User> getDiap(int up, int down) {
        try (Statement statement = this.connection.createStatement()) {
            String q = String.format("SELECT * FROM users WHERE id <= %d AND id >= %d",up,down);
            List<User> users = new ArrayList<User>();
            ResultSet resultSet = statement.executeQuery(q);
            while (resultSet.next()) {
                users.add(new User(resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("pass"),
                        resultSet.getString("role")));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
//-------------------------------------------------------
    public void addU(User user){
        try (PreparedStatement statement = this.connection.prepareStatement
                ("INSERT INTO users(login, pass, role) " +
                        "VALUES(?,?,?)")) {
            statement.setObject (1, user.login);
            statement.setObject (2, user.pass);
            statement.setObject (3, user.role);
            statement.execute();
        } catch (SQLException e){ e.printStackTrace();}
    }
    public void delU(int id) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM users WHERE id = ?")) {
            statement.setObject(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void altU(int id, String newpass) {
        String q = String.format("update users set pass = '%s' where id = %d",newpass,id);
        try (PreparedStatement statement = this.connection.prepareStatement(q)) {
            //statement.setObject(1, newpass);
            //statement.setObject(2, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
