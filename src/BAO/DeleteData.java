/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class DeleteData {

    private Connection con;

    public DeleteData() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteDrug() throws SQLException {
        try {
            String sql = "delete from drugs where id >= 1 ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteLabTest() throws SQLException {
        try {
            String sql2 = "delete from lab_test where id >= 1 ;";
            PreparedStatement stmnt2 = con.prepareStatement(sql2);
            stmnt2.executeUpdate();
            String sql = "delete from lab_group where id >= 1 ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteRegion() throws SQLException {
        try {
            String sql = "delete from region where id >= 1 ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deletPrescription() throws SQLException {
        try {
            String sql = "delete from prescription_detail where id >= 1 ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
            String sql2 = "delete from prescription_no where id >= 1 ;";
            PreparedStatement stmnt2 = con.prepareStatement(sql2);
            stmnt2.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    public void deleteLabOrder() throws SQLException {
        try {
            String sql = "delete from lab_order where id >= 1 ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
            String sql2 = "delete from lab_order_detail where id >= 1 ;";
            PreparedStatement stmnt2 = con.prepareStatement(sql2);
            stmnt2.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    public void deleteMovement() throws SQLException {
        try {
            String sql = "delete from movments where id >= 1 ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
    public void deletePatient() throws SQLException {
        try {
            String sql = "delete from patient where id >= 1 ;";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

}
