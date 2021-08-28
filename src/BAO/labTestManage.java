/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.LabGroup;
import DAO.LabTest;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ayman
 */
public class labTestManage {

    private static Connection con;

    public labTestManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void add(LabTest test) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.lab_test(name,lab_test.group)VALUES(?,?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, test.getTestName());
            stmnt.setInt(2, test.getGroup());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error Add LabTest");
        }

    }

    public ArrayList<LabTest> allLabTest() throws SQLException {
        try {
            String sql = "SELECT * FROM lab_test ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ArrayList<LabTest> allTest = new ArrayList<>();
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                LabTest rg = new LabTest();
                rg.setTestId(rs.getInt(1));
                rg.setTestName(rs.getString(2));
                rg.setGroup(rs.getInt(3));
                allTest.add(rg);
            }
            return allTest;
        } catch (Exception e) {
            throw new SQLException("Error Add LabTest");
        }
    }

    public ArrayList<LabTest> allLabTest(LabGroup group) throws SQLException {
        try {
            String sql = "SELECT * FROM lab_test where lab_test.group = ? ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ArrayList<LabTest> allTest = new ArrayList<>();
            stmnt.setInt(1, group.getId());
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                LabTest rg = new LabTest();
                rg.setTestId(rs.getInt(1));
                rg.setTestName(rs.getString(2));
                rg.setGroup(rs.getInt(3));
                allTest.add(rg);
            }
            return allTest;
        } catch (Exception e) {
            throw new SQLException("Error Add LabTest");
        }
    }

    public void update(LabTest test) throws SQLException {
        try {
            String sql = "update oncology.lab_test set name = ? ,lab_test.group = ? WHERE id = ?  ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, test.getTestName());
            stmnt.setInt(2, test.getGroup());
            stmnt.setInt(3, test.getTestId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }

    }
}
