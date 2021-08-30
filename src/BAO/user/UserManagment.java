/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO.user;

import DAO.user.User;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Ayman
 */
public class UserManagment {

    public int addUser(User user) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "INSERT INTO users(name,password,dept)VALUES(?,?,?)";
            PreparedStatement stmnt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmnt.setString(1, user.getName());
            stmnt.setString(2, user.getPassword());
            stmnt.setInt(3, user.getDept());
            int queryResult = stmnt.executeUpdate();
            ResultSet resultID;
            int result;
            resultID = stmnt.getGeneratedKeys();
            while (resultID.next()) {
                result = Integer.parseInt(resultID.getString(1));
                return result;
            }
        } catch (SQLException e) {
            throw e ; 
        }
        throw new SQLException("Error Save User");
    }

    public boolean update(User user) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "update users set user=?,password=?,dept=? WHERE id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, user.getName());
            stmnt.setString(2, user.getPassword());
            stmnt.setInt(2, user.getDept());
            stmnt.setInt(4, user.getId());
            int result = stmnt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new SQLException("Error Update User");
        }

    }

    public boolean Delete(User user) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "delete FROM users WHERE id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, user.getId());
            int result = stmnt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new SQLException("Error Update User");
        }

    }

    public ArrayList<User> allUser() throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "SELECT * FROM users";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ResultSet result = stmnt.executeQuery();
            ArrayList<User> allUser = new ArrayList<>();
            while (result.next()) {
                User u = new User();
                u.setId(result.getInt(1));
                u.setName(result.getString(2));
                u.setPassword(result.getString(3));
                u.setDept(result.getInt(4));
                allUser.add(u);
            }
            return allUser;
        } catch (SQLException e) {
            throw new SQLException("Error Get User");
        }

    }

    public ArrayList<User> allDoctor() throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "SELECT * FROM users where dept = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, User.DOCTOR);
            ResultSet result = stmnt.executeQuery();
            ArrayList<User> allUser = new ArrayList<>();
            while (result.next()) {
                User u = new User();
                u.setId(result.getInt(1));
                u.setName(result.getString(2));
                u.setPassword(result.getString(3));
                u.setDept(result.getInt(4));
                allUser.add(u);
            }
            return allUser;
        } catch (SQLException e) {
            throw new SQLException("Error Get User");
        }

    }

}
