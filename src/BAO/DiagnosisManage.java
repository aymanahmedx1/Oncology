/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO;

import DAO.Diagnosis;
import DAO.Region;
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
public class DiagnosisManage {

    private static Connection con;

    public DiagnosisManage() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(RegionManage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void add(Diagnosis diagnosis) throws SQLException {
        try {
            String sql = "INSERT INTO oncology.diagnosis(name)VALUES(?) ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, diagnosis.getName());
            stmnt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Error Add diagnosis");
        }

    }

    public ArrayList<Diagnosis> allDiagnosis() throws SQLException {
        try {
            String sql = "SELECT * FROM diagnosis ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            ArrayList<Diagnosis> allDiagnosis = new ArrayList<>();
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()) {
                Diagnosis rg = new Diagnosis();
                rg.setId(rs.getInt(1));
                rg.setName(rs.getString(2));
                allDiagnosis.add(rg);
            }
            return allDiagnosis;
        } catch (Exception e) {
            throw new SQLException("Error get Diagnosis");
        }
    }

    public void update(Diagnosis dia) throws SQLException {
        try {
            String sql = "update oncology.diagnosis set name = ? where id = ?  ; ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setString(1, dia.getName());
            stmnt.setInt(2, dia.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }

    }
}
