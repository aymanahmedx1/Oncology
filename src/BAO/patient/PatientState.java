/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BAO.patient;

import DAO.DeathInfo;
import DAO.patient.Patient;
import commons.DBConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DELL
 */
public class PatientState {

    private Connection con;

    public PatientState() {
        try {
            con = DBConnection.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(PatientState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdatePatientState(int state, Patient pat) throws SQLException {
        try {
            String sql = "update patient set black_list = ? where id = ?";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, state);
            stmnt.setInt(2, pat.getId());
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void addDeathNote(Patient pat, String note) throws SQLException {
        try {
            String sql = "insert into death_note(id,note,date) values (?,?,?)";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            stmnt.setString(2, note);
            stmnt.setDate(3, Date.valueOf(LocalDate.now()));
            stmnt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public DeathInfo getDeathNote(Patient pat) throws SQLException {
        try {
            String sql = "select * from  death_note where id = ? ";
            PreparedStatement stmnt = con.prepareStatement(sql);
            stmnt.setInt(1, pat.getId());
            ResultSet rs = stmnt.executeQuery();
            DeathInfo df = null;
            while (rs.next()) {
                df = new DeathInfo(rs.getInt(1), rs.getString(2), rs.getDate(3));
            }
            return df;
        } catch (SQLException e) {
            throw e;
        }
    }
}
