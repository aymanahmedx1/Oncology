/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO.user;

import DAO.user.Permisions;
import DAO.user.User;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Ayman
 */
public class Authmanage {

    public void updateAuth(Permisions auth) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "UPDATE hospital.auth SET admin=?, patient = ?, manage = ?, users = ?, drugs = ?, reports = ? WHERE (id = ?);";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, auth.getAdmin());
            stmnt.setInt(2, auth.getPatient());
            stmnt.setInt(3, auth.getManage());
            stmnt.setInt(4, auth.getUsers());
            stmnt.setInt(5, auth.getDrugs());
            stmnt.setInt(6, auth.getReports());
            stmnt.setInt(7, auth.getUserId());
            int result = stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Update auth");
        }

    }

    public Permisions getUserAuth(User user) throws SQLException {
        try (Connection con = DBConnection.createConnection();) {
            String sql = "SELECT * FROM auth WHERE id=?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, user.getId());
            ResultSet result = stmnt.executeQuery();
            Permisions auth = new Permisions();
            while (result.next()) {
                auth.setAdmin(result.getInt(2));
                auth.setPatient(result.getInt(3));
                auth.setManage(result.getInt(4));
                auth.setUsers(result.getInt(5));
                auth.setDrugs(result.getInt(6));
                auth.setReports(result.getInt(7));
            }
            return auth;
        } catch (SQLException e) {
            throw new SQLException("Error get Auth");
        }
    }

}
